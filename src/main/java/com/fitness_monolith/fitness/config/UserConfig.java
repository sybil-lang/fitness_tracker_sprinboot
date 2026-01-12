package com.fitness_monolith.fitness.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

//@Configuration
public class UserConfig {

//    @Bean
    public UserDetailsService userDetailsService(
            DataSource dataSource,
            PasswordEncoder passwordEncoder) {

        JdbcUserDetailsManager manager =
                new JdbcUserDetailsManager(dataSource);

        if (!manager.userExists("user2")) {
            manager.createUser(User.withUsername("user2")
                    .password(passwordEncoder.encode("user2"))
                    .roles("USER")
                    .build());
        }

        if (!manager.userExists("admin1")) {
            manager.createUser(User.withUsername("admin1")
                    .password(passwordEncoder.encode("admin1"))
                    .roles("ADMIN")
                    .build());
        }

        return manager;
    }
}
