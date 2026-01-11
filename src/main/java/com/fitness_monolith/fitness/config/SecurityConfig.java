package com.fitness_monolith.fitness.config;

import com.fitness_monolith.fitness.utils.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
//    @Autowired
//    DataSource dataSource;
//
//    @Autowired
//    private AuthTokenFilter authTokenFilter;

    private final DataSource dataSource;
    private final AuthTokenFilter authTokenFilter;

    //  Constructor Injection
    public SecurityConfig(DataSource dataSource, AuthTokenFilter authTokenFilter) {
        this.dataSource = dataSource;
        this.authTokenFilter = authTokenFilter;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                //  Disable CSRF for stateless APIs (JWT)
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // 🔓 Public endpoints (NO authentication)
                        .requestMatchers("/signin", "/hello").permitAll()

                        // 🔐 Role-based access
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                        // 🔒 Everything else needs authentication
                        .anyRequest().authenticated()
                )

                //  Add custom JWT/Auth filter
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)

                //  Disable basic auth (JWT based)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder){
        return builder.getAuthenticationManager();
    }



}


/*

UserDetailsService - to load the user information
UserDetails - it represent the user information

* */

/*
========================================
🔐 AUTHENTICATION: SESSION vs JWT (REVISION)
========================================

JWT = JSON Web Token
👉 A token that contains:
   - User identity
   - Roles / authorities
   - Expiry time
👉 Stateless authentication mechanism

----------------------------------------
🟦 TRADITIONAL SESSION-BASED AUTH
----------------------------------------

Flow:
1️⃣ Client sends username + password
2️⃣ Server validates credentials
3️⃣ Server creates a SESSION
4️⃣ Server stores session data (in memory / DB)
5️⃣ Server sends sessionId (JSESSIONID) to client
6️⃣ Client sends sessionId with every request
7️⃣ Server looks up session data every time

Key Points:
✔ Server-side state is maintained
✔ Session is tied to a specific server
❌ Not easily scalable (problem in multiple servers)
❌ Session replication required in clustering

Example:
- S1, S2 servers need shared session storage
- Load balancer must be session-aware (sticky sessions)

----------------------------------------
🟩 JWT-BASED AUTH (STATELESS)
----------------------------------------

Flow:
1️⃣ Client sends username + password
2️⃣ Server verifies credentials
3️⃣ Server generates JWT (signed token)
4️⃣ Client stores JWT (localStorage / cookie)
5️⃣ Client sends JWT with every request
6️⃣ Server validates JWT signature & expiry
7️⃣ No session lookup required

Key Points:
✔ Stateless (no server-side storage)
✔ Easy horizontal scaling
✔ Works well with microservices
✔ Faster (no DB lookup per request)

----------------------------------------
🧾 WHAT A JWT CONTAINS
----------------------------------------

📦 Header:
- Token type (JWT)
- Algorithm (HS256, RS256)

📦 Payload (Claims):
- username / userId
- roles / authorities
- issuedAt (iat)
- expiry (exp)

📦 Signature:
- Ensures token is not tampered

----------------------------------------
⚠️ IMPORTANT JWT NOTES (BEGINNER MUST KNOW)
----------------------------------------

❗ JWT is NOT encrypted, it is only signed
❗ Anyone can decode JWT, but cannot modify it
❗ Expiry is mandatory (security)
❗ Logout = client deletes token
❗ Token size is bigger than sessionId

----------------------------------------
🆚 SESSION vs JWT (QUICK COMPARISON)
----------------------------------------

Session:
- Stateful ❌
- Stored on server
- Hard to scale
- Easy logout

JWT:
- Stateless ✅
- Stored on client
- Highly scalable
- Logout needs token expiry/blacklist

----------------------------------------
✅ WHEN TO USE JWT
----------------------------------------

✔ REST APIs
✔ Mobile apps
✔ SPA (Angular / React)
✔ Microservices architecture

----------------------------------------
🧠 INTERVIEW ONE-LINER
----------------------------------------

"JWT is a stateless authentication mechanism where
the server does not store session data; instead,
all user information is carried inside the token."

========================================
END OF REVISION ✨
========================================
*/
