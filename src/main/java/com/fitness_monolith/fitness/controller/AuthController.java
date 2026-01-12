package com.fitness_monolith.fitness.controller;


import com.fitness_monolith.fitness.dto.*;
import com.fitness_monolith.fitness.model.User;
import com.fitness_monolith.fitness.respositories.UserRepository;
import com.fitness_monolith.fitness.service.UserService;
import com.fitness_monolith.fitness.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    //  Constructor Injection (Recommended)
    public AuthController(UserService userService,
                          JwtUtils jwtUtils
    ) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(userService.register(registerRequest));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody FitnessLoginRequest request
    ) {

        User user = userService.authenticate(request);

        String token = jwtUtils.generateToken(
                user.getId(),
                user.getRole().name()
        );

        return ResponseEntity.ok(
                new LoginResponse(token, userService.mapToUserResponse(user))
        );
    }

}
