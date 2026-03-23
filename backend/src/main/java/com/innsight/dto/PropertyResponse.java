package com.innsight.dto;

import com.innsight.model.ListingType;

public class PropertyResponse {

    private Long id;
    private String title;
    private String description;
    private Double price;
    private String city;
    private ListingType listingType;
    private String imageUrl;

    public PropertyResponse(Long id, String title, String description, Double price, String city, ListingType listingType, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.city = city;
        this.listingType = listingType;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getCity() {
        return city;
    }

    public ListingType getListingType() {
        return listingType;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
