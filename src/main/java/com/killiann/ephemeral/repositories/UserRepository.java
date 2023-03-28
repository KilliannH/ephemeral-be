package com.killiann.ephemeral.repositories;

import com.killiann.ephemeral.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<UserModel> findByEmail(String email);
    Boolean existsByEmail(String email);
}
