package com.innsight.service;

import com.innsight.dto.AdminPropertyResponse;
import com.innsight.exception.ResourceNotFoundException;
import com.innsight.model.Property;
import com.innsight.repository.PropertyRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminPropertyService {

    private final PropertyRepository propertyRepository;

    public AdminPropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminPropertyResponse> getAllProperties() {

        return propertyRepository.findAll()
                .stream()
                .map(p -> new AdminPropertyResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getOwner().getId(),
                        p.getOwner().getEmail()
                ))
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProperty(Long propertyId) {
        if (!propertyRepository.existsById(propertyId)) {
            throw new ResourceNotFoundException("Property not found");
        }
        propertyRepository.deleteById(propertyId);
    }
}

