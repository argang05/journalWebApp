package com.example.abhijournalwebapp.journalWebApplication.controller;

import com.example.abhijournalwebapp.journalWebApplication.dto.UserDto;
import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.service.UserDetailsServiceImpl;
import com.example.abhijournalwebapp.journalWebApplication.service.UserService;
import com.example.abhijournalwebapp.journalWebApplication.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
@Slf4j
//CrossOrigin Annotation Allows To Prevent Cors Error While Making Request From Frontend To Backend Due to Port Difference
@CrossOrigin
//We can give names to controller using @Tag annotation;
@Tag(name="Public APIs", description = "Contains All Unprotected API Endpoints Which Don't Require Authentication")
public class PublicController {
    /*
    Public Controller for all sorts of api calls which does not require authentication:
     */
    //@GetMapping is basically used for GET requests.

    //Using AuthenticationManager Class to help in assigning JWT Token on Successful authenticated login:
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    //Injecting JwtUtil Class
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Application Running Status: Ok!";
    }

    @PostMapping("/signup")
    @Operation(summary = "SignUp User")
    public ResponseEntity<User> signupUser(@RequestBody UserDto user) {
        //Setting Data From DTO User entity to our actual User Model Entity:
        User userEntity = new User();
        userEntity.setUserName(user.getUserName());
        userEntity.setPassword(user.getPassword());
        userEntity.setEmail(user.getEmail());
        userEntity.setSentimentAnalysis(user.isSentimentAnalysis());

        if (userEntity.getUserName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            userService.saveNewUser(userEntity);
            return new ResponseEntity<>(userEntity,HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception Occurred while signing up user: ",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "SignIn User")
    public ResponseEntity<String> loginUser(@RequestBody User userEntity) {
        try {
            if (userEntity.getUserName().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            //Authenticating user using authentication manager and
            // instance of UsernamePasswordAuthenticationToken
            //It will internally check whether given password matches the encoded one in db to authenticate
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userEntity.getUserName() , userEntity.getPassword()
                    )
            );

            //At this point user is authenticated and we will get the User Credentials build by
            // userDetailsServiceImpl
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(
                    userEntity.getUserName());

            //Assigning jwt-token to user so that from next time we only ask for the token to sign in the user
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            //Passing the jwt token as response:
            return new ResponseEntity<>(jwt,HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception Occurred while authenticating user: ",e);
            return new ResponseEntity<>("Incorrect Username Password",HttpStatus.BAD_REQUEST);
        }
    }
}
