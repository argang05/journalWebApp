package com.example.abhijournalwebapp.journalWebApplication.filter;

// This filter is used to replace the basic HTTP authentication filter provided by Spring Security.
// It processes JWT (JSON Web Token) authentication before the default Spring Security mechanisms.

import com.example.abhijournalwebapp.journalWebApplication.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// The filter processes each HTTP request once, as indicated by extending OncePerRequestFilter.
@Component
public class JwtFilter extends OncePerRequestFilter {

    // This service is used to load user details by username from the database or another source.
    @Autowired
    private UserDetailsService userDetailsService;

    // Utility class to work with JWT tokens (extract username, validate token, etc.).
    @Autowired
    private JwtUtil jwtUtil;

    // Main method of the filter that processes the request.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        /*
        The FilterChain object in the above code is part of the Servlet Filter API and is
        used to pass the current HTTP request and response to the next filter in the filter chain
        (or to the next step in processing if no further filters exist).
         */

        // Retrieve the "Authorization" header from the incoming HTTP request.
        String authorizationHeader = request.getHeader("Authorization");

        // Initialize variables for storing the username and JWT token.
        String username = null;
        String jwt = null;

        // Check if the header is not null and starts with "Bearer " (indicating it contains a JWT).
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract the token part by removing the "Bearer " prefix.
            jwt = authorizationHeader.substring(7);

            // Extract the username from the token using the JwtUtil utility class.
            username = jwtUtil.extractUsername(jwt);
        }

        // If a username is extracted and the user is not already authenticated:
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details (like username, password, roles) from the UserDetailsService.
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token to ensure it is not tampered with and has not expired.
            if (jwtUtil.validateToken(jwt)) {
                // Create an authentication object for the user.
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, // User's details
                        null,       // No credentials are needed (as JWT provides the validation).
                        userDetails.getAuthorities() // User's roles/permissions.
                );

                // Add extra details about the request (e.g., IP address) to the authentication object.
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication object in the SecurityContext so Spring Security knows the user is authenticated.
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Continue processing the request by passing it down the filter chain.
        chain.doFilter(request, response);
    }
}
