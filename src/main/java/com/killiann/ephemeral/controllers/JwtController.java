package com.killiann.ephemeral.controllers;

import com.killiann.ephemeral.jwtutils.JwtUserDetailsService;
import com.killiann.ephemeral.jwtutils.TokenManager;
import com.killiann.ephemeral.models.TempModel;
import com.killiann.ephemeral.repositories.UserRepository;
import com.killiann.ephemeral.models.JwtRequestModel;
import com.killiann.ephemeral.models.JwtResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ResourceBundle;

@RestController
@CrossOrigin
public class JwtController {
    private static final Logger logger = LoggerFactory.getLogger(JwtController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenManager tokenManager;
    @PostMapping("/login")
    public ResponseEntity createToken(@RequestBody JwtRequestModel request) throws Exception {
        logger.debug("[AUTH] - username: " + request.getUsername() + ", password: " + request.getPassword());
        try {
            authenticationManager.authenticate(
                    new
                            UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = tokenManager.generateJwtToken(userDetails);
        return ResponseEntity.ok(new JwtResponseModel(jwtToken));
    }
    @PostMapping("/authenticate")
    public ResponseEntity signIn(@RequestBody TempModel response) throws Exception {
        String subject = tokenManager.getSubjectFromToken(response.accessToken);
        return ResponseEntity.ok(subject);
        /*
        // check if user not exists
        UserModel userByName = userRepository.findByUsername(newUser.getUsername());
        UserModel userByEmail = userRepository.findByEmail(newUser.getEmail());

        if(userByName != null || userByEmail != null) {
            throw new Exception("USER_ALREADY_EXISTS");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

        // load it
        final UserDetails userDetails = userDetailsService.loadNewUser(newUser);
        final String jwtToken = tokenManager.generateJwtToken(userDetails);

        // save it in db
        userRepository.save(newUser);
        return ResponseEntity.ok(new JwtResponseModel(jwtToken)); */
    }
}