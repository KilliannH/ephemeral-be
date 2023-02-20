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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
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

    @Value("${application.name}")
    private String appName;

    @Value("${facebook.apiVersion}")
    private String apiVersion;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponseModel> signIn(@RequestBody TempModel response) throws IOException {
        Optional<Claims> optClaims;
        Optional<JwtResponseModel> optJwtResponseModel = Optional.empty();
        Optional<FbAuthResponse> optFbAuthResponse;
        Optional<FbUserInfoResponse> optFbUserInfoResponse = Optional.empty();

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
                // salts should not be hardcoded (4)
                String decodedFacebookId = CaesarCipher.decrypt(fbAuthResponse.getFacebookId(), 4);
                fbAuthResponse.setFacebookId(decodedFacebookId);

                // check if user exists
                Optional<UserModel> userByFacebookId = userRepository.findByFacebookId(fbAuthResponse.getFacebookId());

                // get his info from Facebook GraphQL if it doesn't exist
                if(!userByFacebookId.isPresent()) {
                    optFbUserInfoResponse = FbUtils.getUserInfo(fbAuthResponse.getAccessToken(), apiVersion);
                }
                // check if user exists
                UserModel connUser = null;
                UserDetails userDetails = null;

                if(userByFacebookId.isPresent()) {
                    userDetails = userDetailsService.loadUserByFacebookId(fbAuthResponse.getFacebookId());
                    connUser = userByFacebookId.get();
                    FbUserInfoResponse fbUserInfoResponse = response.userInfos;

                        HashMap<String, String> toUpdate = new HashMap<>();

                    // TODO - handle this: this always true because facebook returns a new hash for every login
                    if(!Objects.equals(connUser.getImageUrl(), fbUserInfoResponse.imageUrl)) {
                            toUpdate.put("imageUrl", fbUserInfoResponse.imageUrl);
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
                    jwtToken = tokenManager.generateJwtToken(userDetails, connUser.getFacebookId());
                }
                return ResponseEntity.ok(new JwtResponseModel(jwtToken, connUser));
            }
        }
        return ResponseEntity.of(optJwtResponseModel);
    }
}