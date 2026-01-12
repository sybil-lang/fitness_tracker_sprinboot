package com.fitness_monolith.fitness.utils;

import com.fitness_monolith.fitness.model.User;
import com.fitness_monolith.fitness.respositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT Authentication Filter
 *
 * This filter runs ONCE for every HTTP request.
 * It:
 * 1️⃣ Extracts JWT from Authorization header
 * 2️⃣ Validates token
 * 3️⃣ Extracts userId & role
 * 4️⃣ Sets authentication in Spring Security Context
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 🔹 Get Authorization header
        String header = request.getHeader("Authorization");

        // 🔹 Check if header exists and starts with "Bearer "
        if (header != null && header.startsWith("Bearer ")) {

            // 🔹 Extract JWT token (remove "Bearer ")
            String token = header.substring(7);

            try {
                // 🔹 Extract data from token
                String userId = jwtUtils.getUserId(token);
                String role = jwtUtils.getRole(token);

                // 🔹 Fetch user from database
                User user = userRepository.findById(userId).orElse(null);

                if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 🔹 Convert role into Spring Security format
                    // Example: USER → ROLE_USER
                    List<GrantedAuthority> authorities =
                            List.of(new SimpleGrantedAuthority("ROLE_" + role));

                    // 🔹 Create Authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user.getId(),     // principal
                                    null,      // credentials (not needed for JWT)
                                    authorities
                            );

                    // 🔹 Set authentication in Security Context
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("ROLE FROM TOKEN = " + role);
                    System.out.println("AUTHORITIES = " +
                            List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                }
            } catch (Exception e) {
                // 🔹 If token is invalid, just continue without authentication
                // SecurityConfig will block protected endpoints
                System.out.println("JWT Authentication failed: " + e.getMessage());
            }
        }

        // 🔹 Continue filter chain
        filterChain.doFilter(request, response);
    }
}
