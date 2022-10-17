package com.killiann.ephemeral.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import java.util.HashMap;
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
    public ResponseEntity<JwtResponseModel> signIn(@RequestBody TempModel response) throws IOException {
        Optional<Claims> optClaims = Optional.empty();
        Optional<JwtResponseModel> optJwtResponseModel = Optional.empty();
        Optional<FbAuthResponse> optFbAuthResponse = Optional.empty();
        ResourceBundle rb = ResourceBundle.getBundle("config");
        final String appName = rb.getString("application.name");
        String jwtToken = null;

        optClaims = Optional.ofNullable(tokenManager.getClaimsFromToken(response.accessToken, true));

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
                UserModel connUser = null;
                UserDetails userDetails = null;

                if(userByFacebookId.isPresent()) {
                    userDetails = userDetailsService.loadUserByFacebookId(fbAuthResponse.getFacebookId());
                    connUser = userByFacebookId.get();
                    if(optFbUserInfoResponse.isPresent()) {
                        FbUserInfoResponse fbUserInfoResponse = optFbUserInfoResponse.get();
                        JsonObject pictureData = fbUserInfoResponse.picture.getAsJsonObject("data");
                        String imageUrl = pictureData.get("url").getAsString();

                        HashMap<String, String> toUpdate = new HashMap<>();

                        if(!Objects.equals(connUser.getImageUrl(), imageUrl)) {
                            toUpdate.put("imageUrl", imageUrl);
                        }

                        if(!Objects.equals(connUser.getUsername(), fbUserInfoResponse.name)) {
                            toUpdate.put("name", fbUserInfoResponse.name);
                        }

                        if(!Objects.equals(connUser.getEmail(), fbUserInfoResponse.email)) {
                            toUpdate.put("email", fbUserInfoResponse.email);
                        }

                        if(!toUpdate.isEmpty()) {
                            // update what is needed to update
                            if(toUpdate.containsKey("name")) {
                                connUser.setUsername(toUpdate.get("name"));
                            }
                            if(toUpdate.containsKey("email")) {
                                connUser.setEmail(toUpdate.get("email"));
                            }
                            if(toUpdate.containsKey("imageUrl")) {
                                connUser.setImageUrl(toUpdate.get("imageUrl"));
                            }
                            userRepository.save(connUser);
                        }
                    }
                } else {
                    // a new user is created
                    connUser = new UserModel();
                    if(optFbUserInfoResponse.isPresent()) {
                        FbUserInfoResponse userInfoResponse = optFbUserInfoResponse.get();
                        connUser.setFacebookId(fbAuthResponse.getFacebookId());
                        connUser.setUsername(userInfoResponse.name);
                        connUser.setRole("user");
                        connUser.setEmail(userInfoResponse.email);
                        JsonObject pictureData = userInfoResponse.picture.getAsJsonObject("data");
                        String imageUrl = pictureData.get("url").getAsString();
                        connUser.setImageUrl(imageUrl);

                        userDetails = userDetailsService.loadNewUser(connUser);

                        // save it
                        userRepository.save(connUser);
                    }
                }

                if(userDetails != null) {
                    jwtToken = tokenManager.generateJwtToken(userDetails);
                }
                return ResponseEntity.ok(new JwtResponseModel(jwtToken, connUser));
            }
        }
        return ResponseEntity.of(optJwtResponseModel);
    }
}