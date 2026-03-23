package com.innsight.controller;

import com.innsight.dto.OwnerOfferView;
import com.innsight.model.Offer;
import com.innsight.model.OfferDecision;
import com.innsight.service.OfferDecisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/offers")
@PreAuthorize("hasRole('OWNER')")
public class OwnerOfferController {

    private final OfferDecisionService offerDecisionService;

    public OwnerOfferController(OfferDecisionService offerDecisionService) {
        this.offerDecisionService = offerDecisionService;
    }

    @GetMapping
    public List<OwnerOfferView> myOffers() {
        return offerDecisionService.getOffersForMyProperties();
    }

    @GetMapping("/property/{propertyId}")
    public List<OwnerOfferView> offersForProperty(@PathVariable Long propertyId) {
        return offerDecisionService.getOffersForSpecificProperty(propertyId);
    }

    @PostMapping("/{offerId}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long offerId) {
        offerDecisionService.accept(offerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{offerId}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long offerId) {
        offerDecisionService.reject(offerId);
        return ResponseEntity.noContent().build();
    }

}
