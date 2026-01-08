package com.fitness_monolith.fitness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

//this is the DTO- Data transfer object
public class RegisterRequest {

    private String email;
    private  String password;
    private String firstName;
    private String lastName;
}
