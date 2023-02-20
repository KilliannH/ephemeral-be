package com.killiann.ephemeral.repositories;

import com.killiann.ephemeral.models.Event;
import com.killiann.ephemeral.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByFacebookId(String facebookId);
}
