package com.innsight.service;

import com.innsight.dto.CreateOfferRequest;
import com.innsight.exception.ConflictException;
import com.innsight.exception.ResourceNotFoundException;
import com.innsight.model.Offer;
import com.innsight.model.OfferStatus;
import com.innsight.model.Property;
import com.innsight.model.User;
import com.innsight.repository.OfferRepository;
import com.innsight.repository.PropertyRepository;
import com.innsight.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class OfferCreationService {

    private final OfferRepository offerRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public OfferCreationService(
            OfferRepository offerRepository,
            PropertyRepository propertyRepository,
            UserRepository userRepository,
            UserService userService
    ) {
        this.offerRepository = offerRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public boolean createOrUpdateOffer(CreateOfferRequest req, String email) {

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user not found"));

        Property property = propertyRepository.findById(req.propertyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Property not found"));

        Optional<Offer> existingOpt = offerRepository.findByCustomerAndProperty(customer, property);

        if (existingOpt.isPresent()) {
            Offer existing = existingOpt.get();
            if (existing.getStatus() != OfferStatus.PENDING) {
                throw new ConflictException("Your previous offer has already been " + existing.getStatus().name());
            }
            existing.updateOffer(req.offeredAmount(), req.remarks());
            offerRepository.save(existing);
            return true; // Successfully updated
        }

        Offer offer = new Offer(
                customer,
                property,
                req.offeredAmount(),
                req.remarks()
        );

        offerRepository.save(offer);
        return false; // Created fresh
    }

    public Optional<OfferStatus> getMyOfferStatus(Long propertyId) {

        User customer = userService.getCurrentAuthenticatedUser();

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        return offerRepository
                .findByCustomerAndProperty(customer, property)
                .map(Offer::getStatus);
    }




}

