package com.example.abhijournalwebapp.journalWebApplication.controller;

import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.repository.UserRepository;
import com.example.abhijournalwebapp.journalWebApplication.service.UserDetailsServiceImpl;
import com.example.abhijournalwebapp.journalWebApplication.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
//CrossOrigin Annotation Allows To Prevent Cors Error While Making Request From Frontend To Backend Due to Port Difference
@CrossOrigin
@RequestMapping("api/auth/google")
@Slf4j
public class GoogleAuthController {

    //Fetching Client ID and Client Secret Values From YML File using @Value Annotation:
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String authCode){
        try{
            //STEPS TO HANDLE GOOGLE AUTH CALLBACK:

            //1.) Exchange AuthCode For Auth Tokens:
            String tokenEndpoint = "https://oauth2.googleapis.com/token";

            //Sending All Parameters In Form Of MultiValueMap To Handle Response Data:
            // (We are trying to mock what we did in google developers oauth-playground):
            MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
            params.add("code",authCode);
            params.add("client_id",clientId);
            params.add("client_secret",clientSecret);
            params.add("redirect_uri","https://developers.google.com/oauthplayground");
            params.add("grant_type","authorization_code");

            //Configuring Headers:
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); //key1=value1&key2=value2&...

            //Making an HttpEntity Of The Request Body:
            HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(params, headers);

            //Making a POST request to the token-endpoint url:
            //restTemplate.postForEntity(request-to-url, request-http-entity, response-type)
            ResponseEntity<Map> tokenResponseBody = restTemplate.postForEntity(
                    tokenEndpoint , requestEntity, Map.class
            );

            //Extracting the authorization token from response body:
            String idToken = (String) tokenResponseBody.getBody().get("id_token");

            // 2.) Make a call to google's user info api to retrieve user info:
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token="+idToken;

            //Making a GET request to the userInfoUrl url:
            //Since we are ending the token_id appended to the url we don't need to pass and request body:
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);

            //Checking Response Code:
            if(userInfoResponse.getStatusCode() == HttpStatus.OK){
                //Fetching User Info From Response Body:
                Map<String,Object> userInfo = userInfoResponse.getBody();
                //Fetching email from userInfo:
                String email = (String) userInfo.get("email");
                //If email does not exist in our database then we'll create one user with the email and other creds:
                UserDetails userDetails = null;
                 try{
                     userDetails = userDetailsServiceImpl.loadUserByUsername(email);
                 }catch (Exception e){
                     User user = new User();
                     user.setEmail(email);
                     user.setUserName(email);
                     //Generating a random password for the user:
                     user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                     user.setRoles(Arrays.asList("USER"));
                     userRepository.save(user);
                 }
                 //Generating and Providing a JWT Token on Successful Google Signin:
                String jwtToken = jwtUtil.generateToken(email);

                //Send Response Entity With Appropriate Status Code and Jwt Token In Map:
                return ResponseEntity.ok(Collections.singletonMap("jwt-token",jwtToken));
            }
            //Send Response Entity With Appropriate Status Code:
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }catch (Exception e){
            log.error("Error Occurred While Signin In With Google: ",e);
            //Send Response Entity With Appropriate Status Code:
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

/*
URL TO HIT FROM FRONTEND:
https://accounts.google.com/o/oauth2/auth?
client_id=YOUR_CLIENT_ID
    &redirect_uri=YOUR_REDIRECT_URI (api/auth/google/callback)
    &response_type=code
    &scope=email profile
    &access_type=offline
    &prompt=consent

*/
