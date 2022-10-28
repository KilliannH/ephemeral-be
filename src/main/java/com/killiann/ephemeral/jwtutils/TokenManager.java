package com.killiann.ephemeral.jwtutils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class TokenManager implements Serializable {

    @Id
    private long serialVersionUID;
    public static final long TOKEN_VALIDITY = 72 * 60 * 60; // 72h
    @Value("${token.secret}")
    private String jwtSecret;
    public String generateJwtToken(UserDetails userDetails, String facebookId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("facebookId", facebookId);
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }
    public Boolean validateJwtToken(String token, UserDetails userDetails) {
        String username = getClaimsFromToken(token, false).getSubject();
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        Boolean isTokenExpired = claims.getExpiration().before(new Date());
        return (username.equals(userDetails.getUsername()) && !isTokenExpired);
    }
    public Claims getClaimsFromToken(String token, Boolean useKeyBytes) {
        if(useKeyBytes) {
            return Jwts.parser().setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
        } else {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        }
    }
}