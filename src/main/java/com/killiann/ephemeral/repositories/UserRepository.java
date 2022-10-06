package com.killiann.ephemeral.repositories;

import com.killiann.ephemeral.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);
    Optional<UserModel> findByFacebookId(String facebookId);
}
