package com.innsight.model;

import com.innsight.exception.InvalidStateException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "offers",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"customer_id", "property_id"}
        )
)
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* -------- Ownership graph -------- */

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User customer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Property property;

    /* -------- Customer intent -------- */

    @Column(nullable = false)
    private BigDecimal offeredAmount;

    @Column(length = 500, updatable = false)
    private String remarks;

    /* -------- System state -------- */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferStatus status = OfferStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Offer() {}
    public Offer(
            User customer,
            Property property,
            BigDecimal offeredAmount,
            String remarks
    ) {
        this.customer = customer;
        this.property = property;
        this.offeredAmount = offeredAmount;
        this.remarks = remarks;
        this.createdAt = LocalDateTime.now();
    }

    public void updateOffer(BigDecimal amount, String remarks) {
        this.offeredAmount = amount;
        this.remarks = remarks;
    }


    /* -------- Guards -------- */

    public void accept() {
        if (this.status != OfferStatus.PENDING) {
            throw new IllegalStateException("Invalid state transition");
        }
        this.status = OfferStatus.ACCEPTED;
    }

    public void reject() {
        if (this.status != OfferStatus.PENDING) {
            throw new IllegalStateException("Invalid state transition");
        }
        this.status = OfferStatus.REJECTED;
    }


    private void ensurePending() {
        if (this.status != OfferStatus.PENDING) {
            throw new InvalidStateException("Offer already decided");
        }
    }

    public Property getProperty() {
        return property;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getCustomer() {
        return customer;
    }

    public BigDecimal getOfferedAmount() {
        return offeredAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public Long getId() {
        return id;
    }
}

