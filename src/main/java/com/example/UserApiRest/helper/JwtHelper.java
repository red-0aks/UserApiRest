package com.example.UserApiRest.helper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtHelper {
    private static final Key SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long JWT_TOKEN_VALIDITY = 18000 * 100;
    private static final Date expirationDate = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY);

    public String generateToken(String subject){
        return Jwts.builder()
            .setHeaderParam("typ","jwt")
            .setId("id")
            .setSubject(subject)
            .setExpiration(expirationDate)
            .signWith(SECRET)
            .setIssuer("francisco")
            .setIssuedAt(new Date())
            .compact();
    }
}
