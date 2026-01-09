package com.fitness_monolith.fitness.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.authorizeHttpRequests(authorizeRequests->
                authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                .anyRequest().authenticated());

        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
//        UserDetails user1= User.withUsername("user1")
//                .password("{noop}user1")
//                .roles("USER")
//                .build();
//        UserDetails user2= User.withUsername("admin")
//                .password("{noop}admin")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user1,user2);

        JdbcUserDetailsManager manager =
                new JdbcUserDetailsManager(dataSource);

        if (!manager.userExists("user1")) {
            manager.createUser(User.withUsername("user1")
                    .password("{noop}user1")
                    .roles("USER")
                    .build());
        }

        if (!manager.userExists("admin")) {
            manager.createUser(User.withUsername("admin")
                    .password("{noop}admin")
                    .roles("ADMIN")
                    .build());
        }

        return manager;

    }
}


/*

UserDetailsService - to load the user information
UserDetails - it represent the user information

* */