package com.fitness_monolith.fitness.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String sayHello(){
        return "Hello";
    }



    //This is Method-Level Security: Securing Individual API Endpoints
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/admin/hello")
    public String sayAdminHello(){
        return "Hello Admin!";
    }
    @GetMapping("/user/hello")
    public String sayUserHello(){
        return "Hello User!";
    }

}
