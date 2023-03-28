package com.killiann.ephemeral.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.killiann.ephemeral.helpers.CaesarCipher;
import com.killiann.ephemeral.jwtutils.JwtUserDetailsService;
import com.killiann.ephemeral.jwtutils.TokenManager;
import com.killiann.ephemeral.models.*;
import com.killiann.ephemeral.payloads.MessageResponse;
import com.killiann.ephemeral.payloads.SignupRequest;
import com.killiann.ephemeral.payloads.errors.GenericError;
import com.killiann.ephemeral.repositories.RoleRepository;
import com.killiann.ephemeral.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class JwtController {
    private static final Logger logger = LoggerFactory.getLogger(JwtController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenManager tokenManager;

    @Value("${application.name}")
    private String appName;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new GenericError("/signup", "Bad Request", "Username is already in use", 400));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new GenericError("/signup", "Bad Request", "Email is already in use", 400));
        }

        // Create new user
        UserModel user = new UserModel(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Optional<Role> userRoleOpt = roleRepository.findByName(ERole.ROLE_USER);
            if(!userRoleOpt.isPresent()) {
                return ResponseEntity.status(500).body(new GenericError("/signup", "Server error", "Role not found", 500));
            }
            Role userRole = userRoleOpt.get();
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.status(201).body(new MessageResponse("User registered successfully"));
    }
}