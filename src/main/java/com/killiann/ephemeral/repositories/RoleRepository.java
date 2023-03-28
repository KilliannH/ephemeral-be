package com.killiann.ephemeral.repositories;

import com.killiann.ephemeral.models.ERole;
import com.killiann.ephemeral.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
