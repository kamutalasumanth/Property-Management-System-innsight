package com.innsight.controller;

import com.innsight.dto.CreateOfferRequest;
import com.innsight.model.OfferStatus;
import com.innsight.model.User;
import com.innsight.service.OfferCreationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/offers")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerOfferController {

    private final OfferCreationService offerCreationService;

    public CustomerOfferController(OfferCreationService offerCreationService) {
        this.offerCreationService = offerCreationService;
    }

    @PostMapping
    public ResponseEntity<Void> createOffer(
            @RequestBody CreateOfferRequest request,
            Authentication authentication
    ) {
        System.out.println(">> INCOMING OFFER REQUEST");
        System.out.println("Property ID: " + request.propertyId());
        System.out.println("Amount: " + request.offeredAmount());
        System.out.println("Remarks: " + request.remarks());
        System.out.println("Authenticated User: " + authentication.getName());
        
        boolean isUpdate = offerCreationService.createOrUpdateOffer(
                request,
                authentication.getName() // usually email
        );
        
        System.out.println("Offer Updated Status: " + isUpdate);
        
        return isUpdate
            ? ResponseEntity.status(HttpStatus.OK).build()
            : ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/status/{propertyId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OfferStatus> myOfferStatus(@PathVariable Long propertyId) {

        return offerCreationService.getMyOfferStatus(propertyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }


}

