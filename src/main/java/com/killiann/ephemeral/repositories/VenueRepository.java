package com.killiann.ephemeral.repositories;

import com.killiann.ephemeral.models.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Set<Venue>> findAllByLocationId(Long locationId);
}
