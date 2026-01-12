package com.fitness_monolith.fitness.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User {

    //  Primary Key (UUID)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String email;

    private String password;
    private String firstName;
    private String lastName;

    //  Automatically set when record is created
    @CreationTimestamp
    private LocalDateTime createdAt;

    //  Automatically updated on every update
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Enumerated
    private UserRole role=UserRole.USER;

    /*
      One User → Many Activities
     - mappedBy = "user" → user field in Activity entity
     - cascade ALL → save/delete user affects activities
     - orphanRemoval → deleting user deletes activities
    */
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Activity> activities = new ArrayList<>();

    /*
      One User → Many Recommendations
    */
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Recommendation> recommendations = new ArrayList<>();
}
