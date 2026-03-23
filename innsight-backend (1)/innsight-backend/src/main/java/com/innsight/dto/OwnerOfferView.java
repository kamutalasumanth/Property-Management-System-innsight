package com.innsight.dto;

import com.innsight.model.OfferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OwnerOfferView(
        Long offerId,
        Long propertyId,
        String customerName,
        BigDecimal offeredAmount,
        String remarks,
        OfferStatus status,
        LocalDateTime createdAt
) {}

