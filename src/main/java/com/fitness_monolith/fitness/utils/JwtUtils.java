package com.fitness_monolith.fitness.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private  String jwtSecret="sgdg-w3hcb@23n3u3w-3fbf";
    private int jwtExpiration=172800000;

    public String getJwtFromHeader(){
        return "";
    }

    public String generateTokenFromUsername(String userName){
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+jwtExpiration))
                .signWith(key())
                .compact();
    }

    private boolean validateJwtToken(){
        return true;
    }
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
