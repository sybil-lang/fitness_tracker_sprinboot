package com.fitness_monolith.fitness.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recommendation {

    // 🔑 Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Example: AI, COACH, SYSTEM
    private String type;

    // Long recommendation text
    @Column(length = 2000)
    private String recommendation;

    // 📦 JSON lists
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> improvements;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> suggestions;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> safety;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> tags;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

/*
 =========================================================
 📌 Many Recommendations → One User
 =========================================================

 - Many recommendations belong to one user
 - LAZY → user data loads only when accessed
 - user_id column is auto-created as foreign key
 - nullable = false → recommendation must have a user
 - @JsonIgnore prevents infinite JSON loop
- This is the OWNING SIDE of the relationship
 ---------------------------------------------------------
 🔑 Database Details
 ---------------------------------------------------------

 user_id
 👉 is the FOREIGN KEY COLUMN

 fk_recommendation_user
 👉 is the FOREIGN KEY CONSTRAINT NAME

 ---------------------------------------------------------
 🔍 Hibernate generates SQL like:
 ---------------------------------------------------------

 user_id VARCHAR(255) NOT NULL,
 CONSTRAINT fk_recommendation_user
     FOREIGN KEY (user_id)
     REFERENCES user(id)

 =========================================================
*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_recommendation_user"))
    @JsonIgnore
    private User user;

    /*
     📌 Many Recommendations → One Activity
     - Many recommendations are linked to one activity
     - LAZY → activity data loads only when accessed
     - activity_id is auto-created as foreign key column
     - nullable = false → recommendation must belong to an activity
     - fk_recommendation_activity is the DB constraint name
     - @JsonIgnore prevents infinite JSON loop
  - This is the OWNING SIDE of the relationship
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_recommendation_activity"))
    @JsonIgnore
    private Activity activity;
}
