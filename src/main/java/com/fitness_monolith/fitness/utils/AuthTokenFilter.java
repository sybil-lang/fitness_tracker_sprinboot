package com.fitness_monolith.fitness.utils;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;


@Component
public class AuthTokenFilter extends OncePerRequestFilter {


    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public AuthTokenFilter(JwtUtils jwtUtils,
                           UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    /*

    Incoming HTTP Request
        ↓
    AuthTokenFilter (this method)
            ↓
    Extract & validate JWT
            ↓
    Build Authentication object
            ↓
    Store it in SecurityContext
            ↓
    Controller / @PreAuthorize



This filter extracts and validates the JWT from the request, builds a Spring Security Authentication object using username and roles stored in JWT claims, and stores it in the SecurityContext for authorizatio
     */


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

                // 4️⃣ Extract roles from JWT claims
                List<String> roles = jwtUtils.getRolesFromJwtToken(jwt);

                // 5️⃣ Convert roles to GrantedAuthority
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                // 6️⃣ Create Authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                authorities
                        );

                //Attach request details like IP address oor Session ID
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 7️⃣ Save authentication in SecurityContext
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
