package org.home.todo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Component
public class JWTUtil {

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${userSessionExpirationDays}")
    private int userSessionExpirationDays;

    public String generateJWT(String username) {
        return JWT.create()
                .withSubject("userDetails")
                .withClaim("username", username)
                .withIssuer("org.home")
                .withIssuedAt(new Date())
                .withExpiresAt(ZonedDateTime.now().plusDays(userSessionExpirationDays).toInstant())
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String validateJWTAndRetrieveUsername(String jwt) throws JWTVerificationException {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                                        .withSubject("userDetails")
                                        .withIssuer("org.home")
                                        .build();
        return jwtVerifier.verify(jwt).getClaim("username").asString();
    }
}
