package com.fitness_monolith.fitness.utils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

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

    public String generateTokenFromUsername(UserDetails userDetails){
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
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

    //Extract the roles (authorities) stored inside the JWT claims and return them as a List<String> that Spring Security can understand.

    public List<String> getRolesFromJwtToken(String token) {

        Claims claims =
                Jwts.parser()  //creates a JWT parser

                .verifyWith((SecretKey) key()) //verifies the JWT signature .Ensures token is not tampered


                .build()

                 //Validates signature  -> Validates token structure -> Validates expiration
                .parseSignedClaims(token)

                 //Extracts the payload (claims) of the JWT
                .getBody();


        /*
        After this line, you now have access to:
        {
          "sub": "moon",
          "roles": ["ROLE_USER", "ROLE_ADMIN"],
          "iat": 1704954000,
          "exp": 1704957600
        }
        stored inside claims.

        */

        /*
        Read the roles claim
        Why Object?
        1.JWT claims are stored as generic JSON
        2.Java doesn’t know the exact type at runtime.So we first retrieve it as Object
        At this point : rolesObj might be: List<String> or List<Object> or null (if claim missing)
        * */
        Object rolesObj = claims.get("roles");


        //Here we are checking Is rolesObj actually a List?
        //If yes → safely assign it to rolesList
        //List<?> rolesList is a Java generics wildcard declaration
        if (rolesObj instanceof List<?> rolesList) {

            return
                    rolesList.stream()
                    .map(String::valueOf)
                    .toList();
        }

        return List.of(); // fallback – no roles
    }



//    the below code is for Fitness app


    public String generateToken(String userId, String role) {
        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserId(String token) {
        return getClaims(token).getSubject();
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }
}


/*

For implimenting JWT Auth we need below files :-

1.JwtUtils -> helper class

2.SecuritConfig -> centralized security config

3.JwtAuthFilter-> filter for filterchain that intercepts every request for auth

4.CustomUserDetailService: because we have custom user model





* */