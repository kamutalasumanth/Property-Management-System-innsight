package com.innsight.controller;

import com.innsight.dto.PropertyResponse;
import com.innsight.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{propertyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@PathVariable Long propertyId) {
        favoriteService.addFavorite(propertyId);
    }

    @DeleteMapping("/{propertyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long propertyId) {
        favoriteService.removeFavorite(propertyId);
    }

    @GetMapping
    public List<PropertyResponse> myFavorites() {
        return favoriteService.getMyFavorites();
    }
}
