package com.fitness_monolith.fitness.service;

import com.fitness_monolith.fitness.dto.RegisterRequest;
import com.fitness_monolith.fitness.dto.UserResponse;
import com.fitness_monolith.fitness.model.User;
import com.fitness_monolith.fitness.respositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;

    private UserResponse mapToUserResponse(User savedUser) {

        UserResponse response = new UserResponse();

        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setCreatedAt(savedUser.getCreatedAt());

        return response;
    }

    public UserResponse register(RegisterRequest request) {
//        // Create new User entity
//        User user = new User();
//
//        // Map DTO fields to entity fields
//        user.setEmail(request.getEmail());
//        user.setPassword(request.getPassword()); // ⚠️ hash later
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//


        //using builder pattern- helps to create complex object
        User user=User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .build();


        User savedUser = userRepository.save(user);

        // Convert entity → response DTO
        return mapToUserResponse(savedUser);

    }
}
