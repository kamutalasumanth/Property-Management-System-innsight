package com.innsight.service;

import com.innsight.dto.PropertyResponse;
import com.innsight.mapper.PropertyMapper;
import com.innsight.model.Favorite;
import com.innsight.model.Property;
import com.innsight.model.User;
import com.innsight.repository.FavoriteRepository;
import com.innsight.repository.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PropertyRepository propertyRepository;
    private final UserService userService;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           PropertyRepository propertyRepository,
                           UserService userService) {
        this.favoriteRepository = favoriteRepository;
        this.propertyRepository = propertyRepository;
        this.userService = userService;
    }

    // ADD FAVORITE
    @PreAuthorize("hasRole('CUSTOMER')")
    public void addFavorite(Long propertyId) {

        User user = userService.getCurrentAuthenticatedUser();

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Property not found"
                        )
                );

        // Prevent duplicate favorites
        boolean exists = favoriteRepository
                .findByUserAndProperty(user, property)
                .isPresent();

        if (exists) {
            return; // idempotent
        }

        favoriteRepository.save(new Favorite(user, property));
    }

    // REMOVE FAVORITE
    @PreAuthorize("hasRole('CUSTOMER')")
    public void removeFavorite(Long propertyId) {

        User user = userService.getCurrentAuthenticatedUser();

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Property not found"
                        )
                );

        Favorite favorite = favoriteRepository
                .findByUserAndProperty(user, property)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Favorite not found"
                        )
                );

        favoriteRepository.delete(favorite);
    }

    // LIST MY FAVORITES
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<PropertyResponse> getMyFavorites() {

        User user = userService.getCurrentAuthenticatedUser();

        return favoriteRepository.findByUser(user)
                .stream()
                .map(f -> PropertyMapper.toResponse(f.getProperty()))

                .toList();
    }
}
