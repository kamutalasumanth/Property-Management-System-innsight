package com.innsight.controller;

import com.innsight.model.Offer;
import com.innsight.service.AdminOfferService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/offers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOfferController {

    private final AdminOfferService adminOfferService;

    public AdminOfferController(AdminOfferService adminOfferService) {
        this.adminOfferService = adminOfferService;
    }

    @GetMapping
    public List<Offer> listOffers() {
        return adminOfferService.getAllOffers();
    }
}

