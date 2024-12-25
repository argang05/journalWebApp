package com.example.abhijournalwebapp.journalWebApplication.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
JSON Web Token (JWT) is an open standard (RFC 7519) that defines a compact and self-contained way
for securely transmitting information between parties as a JSON object.
 */
/*
In its compact form, JSON Web Tokens consist of three parts separated by dots (.), which are:
Header
Payload
Signature
Therefore, a JWT typically looks like the following : "xxxxx.yyyyy.zzzzz"
 */

//All frequently used methods of JWT will be here

@Component
public class JwtUtil {

    // Secret key used to sign and verify the JWT token. It must be kept secure.
    private String SECRET_KEY = "8f3Q$yH1%zM@rLd5t!F#9jXp*N6AoU2wVk7gCvBqWE4TYxOsZJm";

    // Method to generate a JWT token for a given username.
    public String generateToken(String username) {
        // Claims are the data included in the token payload (e.g., user information).
        Map<String, Object> claims = new HashMap<>();

        // Call the createToken method to generate the actual token.
        return createToken(claims, username);
    }

    // Method to create a JWT token with the given claims and subject (username).
    private String createToken(Map<String, Object> claims, String subject) {
        // Use the JJWT library to build the token with required details.
        return Jwts.builder()
                .claims(claims) // Add claims (data) to the token payload.
                .subject(subject) // The subject is the main identifier (e.g., username).
                .header() // Define custom header for the JWT.
                .empty() // Start with an empty header.
                .add("typ", "JWT") // Add the type as JWT to the header.
                .and() // Return to the main builder after modifying the header.
                .issuedAt(new Date(System.currentTimeMillis())) // Set the token creation time.
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Set token expiration time (1 hour in this case).
                .signWith(getSigningKey()) // Sign the token using the secret key.
                .compact(); // Finalize and return the token as a string.
    }

    // Method to get the signing key required for signing/verifying the token.
    private SecretKey getSigningKey() {
        // Convert the secret key string into a format accepted by the JWT library.
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Method to validate a JWT token by checking if it has expired.
    public Boolean validateToken(String token) {
        // Return true if the token is not expired.
        return !isTokenExpired(token);
    }

    // Method to extract the username (subject) from the token.
    public String extractUsername(String token) {
        // Extract all claims from the token and get the subject (username).
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    // Method to extract the expiration time of the token.
    public Date extractExpiration(String token) {
        // Extract all claims from the token and get the expiration time.
        return extractAllClaims(token).getExpiration();
    }

    // Method to extract all claims (data) from the token.
    private Claims extractAllClaims(String token) {
        // Use the JWT library to parse and verify the token and get its payload (claims).
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Verify the token using the signing key.
                .build() // Build the parser.
                .parseSignedClaims(token) // Parse the token and extract signed claims.
                .getPayload(); // Retrieve the claims (payload) from the token.
    }

    // Method to check if the token has expired.
    private Boolean isTokenExpired(String token) {
        // Compare the token's expiration time with the current time.
        return extractExpiration(token).before(new Date());
    }
}
