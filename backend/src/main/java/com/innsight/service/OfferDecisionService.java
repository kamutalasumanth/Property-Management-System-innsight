package com.innsight.service;

import com.innsight.dto.OwnerOfferView;
import com.innsight.exception.ConflictException;
import com.innsight.exception.ResourceNotFoundException;
import com.innsight.model.Offer;
import com.innsight.model.OfferStatus;
import com.innsight.model.User;
import com.innsight.repository.OfferRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OfferDecisionService {

    private final OfferRepository offerRepository;
    private final UserService userService;

    public OfferDecisionService(
            OfferRepository offerRepository,
            UserService userService
    ) {
        this.offerRepository = offerRepository;
        this.userService = userService;
    }

    // =========================
    // OWNER: VIEW OFFERS
    // =========================
    @PreAuthorize("hasRole('OWNER')")
    public List<OwnerOfferView> getOffersForMyProperties() {
        User owner = userService.getCurrentAuthenticatedUser();

        return offerRepository.findAllByPropertyOwner(owner)
                .stream()
                .map(offer -> new OwnerOfferView(
                        offer.getId(),
                        offer.getProperty().getId(),
                        offer.getCustomer().getName(),   // or getEmail(), based on your User
                        offer.getOfferedAmount(),
                        offer.getRemarks(),
                        offer.getStatus(),
                        offer.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // =========================
    // OWNER: VIEW PROPERTY OFFERS
    // =========================
    @PreAuthorize("hasRole('OWNER')")
    public List<OwnerOfferView> getOffersForSpecificProperty(Long propertyId) {
        User owner = userService.getCurrentAuthenticatedUser();
        
        System.out.println(">> DB QUERY: Fetching Offers for PropertyID: " + propertyId + " | OwnerID: " + owner.getId());

        List<Offer> rawOffers = offerRepository.findAllByPropertyIdAndPropertyOwner(propertyId, owner);
        
        System.out.println(">> DB QUERY SUCCESS: Found " + rawOffers.size() + " active offers.");

        List<OwnerOfferView> mappedOffers = rawOffers.stream()
                .map(offer -> new OwnerOfferView(
                        offer.getId(),
                        offer.getProperty().getId(),
                        offer.getCustomer().getName(),
                        offer.getOfferedAmount(),
                        offer.getRemarks(),
                        offer.getStatus(),
                        offer.getCreatedAt()
                ))
                .collect(Collectors.toList());
                
        System.out.println(">> API RESPONSE MAPPED: " + mappedOffers);
        return mappedOffers;
    }

    // =========================
    // OWNER: ACCEPT OFFER
    // =========================
    @PreAuthorize("hasRole('OWNER')")
    public void accept(Long offerId) {

        Offer offer = getOwnedPendingOfferOrThrow(offerId);
        offer.accept();
    }

    // =========================
    // OWNER: REJECT OFFER
    // =========================
    @PreAuthorize("hasRole('OWNER')")
    public void reject(Long offerId) {

        Offer offer = getOwnedPendingOfferOrThrow(offerId);
        offer.reject();
    }

    // =========================
    // INTERNAL HELPERS
    // =========================
    private Offer getOwnedPendingOfferOrThrow(Long offerId) {

        User owner = userService.getCurrentAuthenticatedUser();

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Offer not found")
                );

        // 🔒 Ownership enforcement (404 to avoid leakage)
        if (!offer.getProperty().getOwner().equals(owner)) {
            throw new ResourceNotFoundException("Offer not found");
        }

        // 🔒 State transition enforcement
        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new ConflictException("Offer already decided");
        }

        return offer;
    }
}
