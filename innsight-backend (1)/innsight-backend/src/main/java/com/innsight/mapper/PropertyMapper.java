package com.innsight.mapper;

import com.innsight.dto.PropertyResponse;
import com.innsight.model.Property;

public class PropertyMapper {

    public static PropertyResponse toResponse(Property p) {
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
}
