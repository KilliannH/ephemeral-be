package com.killiann.ephemeral.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.killiann.ephemeral.CaesarCipher;
import com.killiann.ephemeral.jwtutils.JwtUserDetailsService;
import com.killiann.ephemeral.jwtutils.TokenManager;
import com.killiann.ephemeral.models.FbAuthResponse;
import com.killiann.ephemeral.models.TempModel;
import com.killiann.ephemeral.repositories.UserRepository;
import com.killiann.ephemeral.models.JwtModel;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/authenticate")
    public ResponseEntity signIn(@RequestBody TempModel response) throws Exception {
        Claims claims = null;
        FbAuthResponse fbAuthResponse = null;
        try {
            claims = tokenManager.getClaimsFromToken(response.accessToken);
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        }
        if(claims != null) {
            String issuer = claims.getIssuer();
            if(!issuer.equals("Ephemeral")) {
                throw new Error("Unrecognized token");
            }
            Gson g = new Gson();
            fbAuthResponse = g.fromJson(claims.getSubject(), FbAuthResponse.class);
            String decodedFacebookId = CaesarCipher.decrypt(fbAuthResponse.getFacebookId(), 4);
            fbAuthResponse.setFacebookId(decodedFacebookId);
        }
        return ResponseEntity.ok(fbAuthResponse);
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