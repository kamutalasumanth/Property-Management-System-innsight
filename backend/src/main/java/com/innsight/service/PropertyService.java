package com.innsight.service;

import com.innsight.dto.CreatePropertyRequest;
import com.innsight.dto.UpdatePropertyRequest;
import com.innsight.model.Property;
import com.innsight.model.User;
import com.innsight.repository.FavoriteRepository;
import com.innsight.repository.OfferRepository;
import com.innsight.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserService userService;
    private final OfferRepository offerRepository;
    private final FavoriteRepository favoriteRepository;

    public PropertyService(PropertyRepository propertyRepository,
                           UserService userService, OfferRepository offerRepository, FavoriteRepository favoriteRepository) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.offerRepository = offerRepository;
        this.favoriteRepository = favoriteRepository;
    }

    // CREATE (OWNER only)
    @PreAuthorize("hasRole('OWNER')")
    public Property createProperty(CreatePropertyRequest request) {

        User owner = userService.getCurrentAuthenticatedUser();

        Property property = new Property();
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        property.setCity(request.getCity());
        property.setListingType(request.getListingType());
        property.setImageUrl(request.getImageUrl());
        property.setOwner(owner);

        return propertyRepository.save(property);
    }

    // LIST MY PROPERTIES (OWNER only)
    @PreAuthorize("hasRole('OWNER')")
    public List<Property> getMyProperties() {

        User owner = userService.getCurrentAuthenticatedUser();
        return propertyRepository.findByOwner(owner);
    }

    // UPDATE MY PROPERTY (OWNER + OWNERSHIP)
    @PreAuthorize("hasRole('OWNER')")
    public Property updateProperty(Long propertyId, UpdatePropertyRequest request) {

        User owner = userService.getCurrentAuthenticatedUser();
        Property property = getOwnedPropertyOrThrow(propertyId, owner);

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        property.setCity(request.getCity());
        property.setListingType(request.getListingType());
        property.setImageUrl(request.getImageUrl());

        return propertyRepository.save(property);
    }



    // DELETE MY PROPERTY (OWNER + OWNERSHIP)
    @PreAuthorize("hasRole('OWNER')")
    @Transactional
    public void deleteProperty(Long id) {

        User owner = userService.getCurrentAuthenticatedUser();
        Property property = getOwnedPropertyOrThrow(id,owner);

        // 1️⃣ Clean dependent data
        favoriteRepository.deleteAllByPropertyId(id);
        offerRepository.deleteAllByPropertyId(id);

        // 2️⃣ Delete property
        propertyRepository.delete(property);
    }



    private Property getOwnedPropertyOrThrow(Long propertyId, User owner) {
        return propertyRepository
                .findByIdAndOwner(propertyId, owner)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Property not found"
                        )
                );
    }

    public Property getMyPropertyById(Long id) {
        User owner = userService.getCurrentAuthenticatedUser();
        return propertyRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Property not found"));
    }
}
