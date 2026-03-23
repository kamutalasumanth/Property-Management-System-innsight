package com.innsight.repository;

import com.innsight.model.Property;
import com.innsight.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByOwner(User owner);

    Optional<Property> findByIdAndOwner(Long id, User owner);
}
