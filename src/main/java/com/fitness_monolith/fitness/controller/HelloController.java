package com.fitness_monolith.fitness.controller;

import com.fitness_monolith.fitness.dto.LoginRequest;
import com.fitness_monolith.fitness.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    //  Constructor Injection (Recommended)
    public HelloController(AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }



    // 🔓 Public API (no authentication required)
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello";
    }

    /**
     * 🔐 Method-Level Security
     * Accessible by users having ROLE_ADMIN or ROLE_USER
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/admin/hello")
    public String sayAdminHello() {
        return "Hello Admin!";
    }

    /**
     * 🔐 Accessible by authenticated users
     */
    @GetMapping("/user/hello")
    public String sayUserHello() {
        return "Hello User!";
    }


    /**
     * 🔑 Login API
     * - Authenticates username & password
     * - Generates JWT token on success
     */
    @PostMapping("/signin")
    public String login(@RequestBody LoginRequest loginRequest) {

        Authentication authentication;
        try {

            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            // Store authentication in SecurityContext
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            // Generate JWT token
            return jwtUtils.generateTokenFromUsername(
                    userDetails.getUsername());

        } catch (AuthenticationException e) {
            return " Invalid username or password";
        }

//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
//        assert userDetails != null;
//        String jwtToken=jwtUtils.generateTokenFromUsername(userDetails.getUsername());
//        return jwtToken;
    }



}
