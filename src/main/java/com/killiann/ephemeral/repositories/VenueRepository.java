package com.killiann.ephemeral.repositories;

import com.killiann.ephemeral.models.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> { }
