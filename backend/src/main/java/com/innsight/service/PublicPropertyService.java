package com.innsight.service;

import com.innsight.dto.PropertyResponse;
import com.innsight.model.Property;
import com.innsight.repository.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PublicPropertyService {

    private final PropertyRepository propertyRepository;

    public PublicPropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    private PropertyResponse mapToResponse(Property p) {
        return new PropertyResponse(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getPrice(),
                p.getCity(),
                p.getListingType(),
                p.getImageUrl()
        );
    }


    public List<PropertyResponse> getAllProperties() {
        return propertyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PropertyResponse getPropertyById(Long id) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Property not found"
                        )
                );

        return mapToResponse(property);
    }

}

