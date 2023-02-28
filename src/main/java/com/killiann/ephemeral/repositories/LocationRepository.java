package com.killiann.ephemeral.repositories;


import com.killiann.ephemeral.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCity(String city);
}
