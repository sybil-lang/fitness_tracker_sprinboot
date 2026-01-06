package com.fitness_monolith.fitness.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class Activity {

    // 🔑 Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // 🏃 RUNNING, WALKING, etc.
    @Enumerated(EnumType.STRING)
    private ActivityType type;

    /*
     📦 Flexible JSON data
     Example:
     {
       "distanceKm": 5.2,
       "steps": 8000
     }
    */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> additionalMetrics;

    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /*
     📌 Many Activities → One User
     - Many activities belong to one user
     - LAZY → user data is loaded only when accessed
     - user_id is auto-created as foreign key column
     - nullable = false → every activity must have a user
     - fk_activity_user is the DB foreign key constraint name
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_activity_user")
    )
    @JsonIgnore
    private User user;


    /*
     📌 One Activity → Many Recommendations
     - One activity can have many recommendations
     - mappedBy = "activity" → activity field in Recommendation owns the relation
     - cascade ALL → save/delete activity affects recommendations
     - orphanRemoval → removing activity deletes its recommendations
     - LAZY → recommendations loaded only when accessed
    */
    @OneToMany(
            mappedBy = "activity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Recommendation> recommendations = new ArrayList<>();
}


/*
 =========================================================
 📌 mappedBy – Inverse Side of Relationship
 =========================================================

 mappedBy is used on the inverse side of a
 bidirectional relationship.

 mappedBy tells Hibernate:
 "I am NOT the owner of this relationship.
  The foreign key is managed by the other entity."

 =========================================================
 📌 Owning Side of Relationship
 =========================================================

 The side where you write:

 @JoinColumn(
     name = "activity_id",
     foreignKey = @ForeignKey(
         name = "fk_recommendation_activity"
     )
 )

 🔥 This side OWNS the relationship because:
 - It defines the foreign key column
 - It defines the foreign key constraint
 - It controls how the relationship is stored in the DB

 =========================================================
*/
