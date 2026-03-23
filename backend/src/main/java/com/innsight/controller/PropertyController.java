package com.innsight.controller;

import com.innsight.dto.CreatePropertyRequest;
import com.innsight.dto.UpdatePropertyRequest;
import com.innsight.model.Property;
import com.innsight.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    // CREATE
    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Property> create(
            @Valid @RequestBody CreatePropertyRequest request) {

        Property createdProperty = propertyService.createProperty(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdProperty);
    }

    // LIST MY PROPERTIES
    @GetMapping("/mine")
    @PreAuthorize("hasRole('OWNER')")
    public List<Property> myProperties() {
        return propertyService.getMyProperties();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public Property getMyProperty(@PathVariable Long id) {
        return propertyService.getMyPropertyById(id);
    }


    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public Property update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePropertyRequest request) {

        return propertyService.updateProperty(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        propertyService.deleteProperty(id);
    }
}
