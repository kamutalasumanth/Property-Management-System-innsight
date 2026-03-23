package com.innsight.repository;

import com.innsight.model.Offer;
import com.innsight.model.OfferStatus;
import com.innsight.model.Property;
import com.innsight.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    Optional<Offer> findByIdAndPropertyOwner(Long id, User owner);

    List<Offer> findAllByPropertyOwner(User owner);

    boolean existsByCustomerAndPropertyAndStatus(
            User customer,
            Property property,
            OfferStatus status
    );

    @org.springframework.data.jpa.repository.Query("SELECT o FROM Offer o WHERE o.property.id = :propertyId AND o.property.owner = :owner")
    List<Offer> findAllByPropertyIdAndPropertyOwner(
            @org.springframework.data.repository.query.Param("propertyId") Long propertyId, 
            @org.springframework.data.repository.query.Param("owner") User owner
    );

    void deleteAllByPropertyId(Long propertyId);

    Optional<Offer> findByCustomerAndProperty(User customer, Property property);


}

