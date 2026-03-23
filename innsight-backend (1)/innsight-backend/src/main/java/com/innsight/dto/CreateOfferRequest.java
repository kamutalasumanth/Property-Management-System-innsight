package com.innsight.dto;

import java.math.BigDecimal;

public record CreateOfferRequest(
        Long propertyId,
        BigDecimal offeredAmount,
        String remarks
) {}
