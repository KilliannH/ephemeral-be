package com.killiann.ephemeral.controllers;

import com.google.gson.Gson;
import com.killiann.ephemeral.helpers.CaesarCipher;
import com.killiann.ephemeral.helpers.FbUtils;
import com.killiann.ephemeral.jwtutils.JwtUserDetailsService;
import com.killiann.ephemeral.jwtutils.TokenManager;
import com.killiann.ephemeral.models.*;
import com.killiann.ephemeral.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
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

    @PostMapping("/authenticate")
    public ResponseEntity<JwtModel> signIn(@RequestBody TempModel response) throws IOException {
        Optional<Claims> optClaims = Optional.empty();
        Optional<JwtModel> optJwtModel = Optional.empty();
        Optional<FbAuthResponse> optFbAuthResponse = Optional.empty();
        ResourceBundle rb = ResourceBundle.getBundle("config");
        final String appName = rb.getString("application.name");
        String jwtToken = null;

        optClaims = Optional.ofNullable(tokenManager.getClaimsFromToken(response.accessToken));

        if(optClaims.isPresent()) {
            Claims claims = optClaims.get();
            String issuer = claims.getIssuer();
            if(!issuer.equals(appName)) {
                throw new Error("Unrecognized token");
            }
            Gson g = new Gson();
            optFbAuthResponse = Optional.ofNullable(g.fromJson(claims.getSubject(), FbAuthResponse.class));
            if(optFbAuthResponse.isPresent()) {
                FbAuthResponse fbAuthResponse = optFbAuthResponse.get();
                String decodedFacebookId = CaesarCipher.decrypt(fbAuthResponse.getFacebookId(), 4);
                fbAuthResponse.setFacebookId(decodedFacebookId);

                // get his info from Facebook GraphQL
                Optional<FbUserInfoResponse> optFbUserInfoResponse = FbUtils.getUserInfo(fbAuthResponse.getAccessToken());

                // check if user exists
                Optional<UserModel> userByFacebookId = userRepository.findByFacebookId(fbAuthResponse.getFacebookId());
                UserDetails userDetails = null;

                if(userByFacebookId.isPresent()) {
                    userDetails = userDetailsService.loadUserByFacebookId(fbAuthResponse.getFacebookId());
                    UserModel connUser = userByFacebookId.get();
                    if(optFbUserInfoResponse.isPresent()) {
                        FbUserInfoResponse fbUserInfoResponse = optFbUserInfoResponse.get();
                        if(!Objects.equals(connUser.getUsername(), fbUserInfoResponse.name)) {
                            // fb doesn't return same name from the API, so we update it.
                            connUser.setUsername(fbUserInfoResponse.name);
                            userRepository.save(connUser);
                        }
                    }
                } else {
                    // a new user is created
                    UserModel newUser = new UserModel();
                    if(optFbUserInfoResponse.isPresent()) {
                        FbUserInfoResponse userInfoResponse = optFbUserInfoResponse.get();
                        newUser.setFacebookId(fbAuthResponse.getFacebookId());
                        newUser.setUsername(userInfoResponse.name);
                        userDetails = userDetailsService.loadNewUser(newUser);

                        // save it
                        userRepository.save(newUser);
                    }
                }

                if(userDetails != null) {
                    jwtToken = tokenManager.generateJwtToken(userDetails);
                }
                return ResponseEntity.ok(new JwtModel(jwtToken));
            }
        }
        return ResponseEntity.of(optJwtModel);
    }
}