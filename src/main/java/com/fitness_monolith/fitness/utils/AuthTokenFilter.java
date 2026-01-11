package com.fitness_monolith.fitness.utils;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


@Component
public class AuthTokenFilter extends OncePerRequestFilter {


    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public AuthTokenFilter(JwtUtils jwtUtils,
                           UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println(" AuthTokenFilter called for: " + request.getRequestURI());

        try {
            // 1️⃣ Extract JWT from Authorization header
            String jwt = jwtUtils.getJwtFromHeader(request);

            // 2️⃣ Validate JWT
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // 3️⃣ Extract username from JWT
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 4️⃣ Load user details from DB
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                // 5️⃣ Create Authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 6️⃣ Set authentication in SecurityContext
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception e) {
            System.out.println(" Cannot set user authentication: " + e.getMessage());
        }

        // 7️⃣ Continue filter chain
        filterChain.doFilter(request, response);
    }
}


/*
 * ===================== Spring Security Request Flow =====================
 *
 * 1️⃣ Client sends an HTTP request
 *
 * 2️⃣ Filter Chain
 *    - Request first enters the Spring Security Filter Chain
 *    - Contains multiple security filters (executed in order)
 *      Examples:
 *        - Authentication Filter (JWT / UsernamePassword)
 *        - Authorization Filter
 *        - CSRF Filter
 *        - CORS Filter
 *
 * 3️⃣ Security Context
 *    - During authentication, user details are stored here
 *    - Holds Authentication object (principal, roles, authorities)
 *    - Accessible anywhere in the app via SecurityContextHolder
 *
 * 4️⃣ DispatcherServlet
 *    - Central front controller of Spring MVC
 *    - Receives request ONLY AFTER passing security filters
 *    - Responsible for routing request to correct controller
 *
 * 5️⃣ Controllers
 *    - @RestController / @Controller handles the request
 *    - Business logic executes here
 *    - Uses authentication info from Security Context if needed
 *
 * ❌ If authentication/authorization fails:
 *    - Request is blocked in Filter Chain
 *    - Controller is NEVER reached
 *
 * =======================================================================
 */


/*
 * Client Request
 *      |
 *      v
 * +--------------------+
 * |   Filter Chain     |   <-- Security Filters
 * |  (Auth, JWT, etc.) |
 * +--------------------+
 *      |
 *      |----> Security Context
 *      |        (Stores logged-in user details)
 *      |
 *      v
 * +--------------------+
 * | DispatcherServlet  |   <-- Spring MVC Front Controller
 * +--------------------+
 *      |
 *      v
 * +--------------------+
 * |   Controllers      |   <-- @RestController
 * +--------------------+
 */
