package com.example.abhijournalwebapp.journalWebApplication.controller;

import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    /*
    Public Controller for all sorts of api calls which does not require authentication:
     */
    //@GetMapping is basically used for GET requests.

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Application Running Status: Ok!";
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> createNewUser(@RequestBody User userEntity) {
        if (userEntity.getUserName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            userService.saveNewUser(userEntity);
            return new ResponseEntity<>(userEntity,HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
