package com.killiann.ephemeral.repositories;

import com.killiann.ephemeral.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> { }
