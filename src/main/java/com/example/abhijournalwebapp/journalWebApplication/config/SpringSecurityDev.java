package com.example.abhijournalwebapp.journalWebApplication.config;

//Configuration Code To Customize Spring Security Features:

// Importing the necessary classes and annotations for the configuration.
import com.example.abhijournalwebapp.journalWebApplication.filter.JwtFilter;
import com.example.abhijournalwebapp.journalWebApplication.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Mark this class as a configuration class to define beans for Spring Security.
@Configuration
// Enable Spring Security for the application.
@EnableWebSecurity
//We can also do profiling on classes to specify that a particular class will execute
// on a certain profile only (Eg: dev or prod) using @Profile annotation
@Profile("dev")
public class SpringSecurityDev {

    // Injecting a custom implementation of UserDetailsService.
    // This is used to fetch user details (like username, password, and roles) from the database.
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Configures the security rules for HTTP requests.
     *
     * @param http The HttpSecurity object to define security configurations.
     * @return SecurityFilterChain The chain of security filters applied to incoming requests.
     * @throws Exception If there is an error during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Authorizing requests based on their paths.
                .authorizeHttpRequests(request -> request
                        // Allow anyone to access URLs starting with /public/** without logging in.
                        .requestMatchers("/public/**").permitAll()
                        // Require users to log in for URLs starting with /journal/** or /user/**.
                        .requestMatchers("/journal/**", "/user/**").authenticated()
                        // Restrict URLs starting with /admin/** to users with the "ADMIN" role only.
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Any other request also requires authentication by default.
                        .anyRequest().permitAll())
                // Disable CSRF protection (Cross-Site Request Forgery) for simplicity in this setup.
                .csrf(AbstractHttpConfigurer::disable)
                //Add jwtFilter authentication before the default form login auth of SpringSecurity
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // Build and return the configured SecurityFilterChain.
                .build();

    }

    /**
     * Configures the authentication manager to use our custom UserDetailsService
     * and the password encoder for verifying user credentials.
     *
     * @param auth The AuthenticationManagerBuilder to configure.
     * @throws Exception If there is an error during configuration.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Setting up the custom user details service and the password encoder.
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    /**
     * Defines a bean for encoding passwords using BCrypt.
     * This ensures that passwords are securely hashed before storage.
     *
     * @return PasswordEncoder An instance of BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Using BCrypt to hash passwords securely.
        return new BCryptPasswordEncoder();
    }

    //Create a bean of AuthenticationManager as an implementation to return instance of it:
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }
}
