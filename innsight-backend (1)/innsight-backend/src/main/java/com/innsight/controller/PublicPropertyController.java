package com.innsight.controller;

import com.innsight.dto.PropertyResponse;
import com.innsight.service.PublicPropertyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/properties")
public class PublicPropertyController {

    private final PublicPropertyService publicPropertyService;

    public PublicPropertyController(PublicPropertyService publicPropertyService) {
        this.publicPropertyService = publicPropertyService;
    }

    // LIST ALL PROPERTIES (public)
    @GetMapping
    public List<PropertyResponse> list() {
        return publicPropertyService.getAllProperties();
    }

    // VIEW PROPERTY DETAILS (public)
    @GetMapping("/{id}")
    public PropertyResponse get(@PathVariable Long id) {
        return publicPropertyService.getPropertyById(id);
    }
}

