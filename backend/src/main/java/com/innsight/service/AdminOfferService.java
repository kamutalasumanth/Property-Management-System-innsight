package com.innsight.service;

import com.innsight.model.Offer;
import com.innsight.repository.OfferRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOfferService {

    private final OfferRepository offerRepository;

    public AdminOfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }
}

