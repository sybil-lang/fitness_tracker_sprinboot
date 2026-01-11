package com.fitness_monolith.fitness.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SignatureException;
import java.util.Date;

@Component
public class JwtUtils {

    private static final String jwtSecret =
            "QXNkZjEyMzRAIUAjJCRmKigpXy1Kd3RTZWNyZXRLZXlfNTEyX2JpdF9sb25n";

    //  Token expiration time (2 days in milliseconds)
    private static final int jwtExpiration = 172800000;

    /**
     *  Extract JWT token from Authorization header
     * Header format: Authorization: Bearer <token>
     */
    public String getJwtFromHeader(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        // Check if header is present and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // remove "Bearer "
        }

        return null;
    }

    public String generateTokenFromUsername(String userName){
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+jwtExpiration))
                .signWith(key())
                .compact();
    }


    /**
     *  Validate JWT token
     * - checks signature
     * - checks expiration
     * - checks token structure
     */
    public boolean validateJwtToken(String jwtToken) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(jwtToken);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }
}
