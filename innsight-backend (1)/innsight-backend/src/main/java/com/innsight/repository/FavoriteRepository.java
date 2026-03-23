package com.innsight.repository;

import com.innsight.model.Favorite;
import com.innsight.model.Property;
import com.innsight.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserAndProperty(User user, Property property);

    List<Favorite> findByUser(User user);

    void deleteAllByPropertyId(Long propertyId);

}
