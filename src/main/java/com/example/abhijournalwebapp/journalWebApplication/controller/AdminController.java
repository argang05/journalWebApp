package com.example.abhijournalwebapp.journalWebApplication.controller;

//Admin Controller For Defining API Calls For Admin Specific And Exclusive Tasks:

import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        if (allUsers != null && !allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create-new-admin")
    public ResponseEntity<?> createNewAdmin(@RequestBody User adminUser){
        try{
            userService.saveNewAdmin(adminUser);
            return new ResponseEntity<>(adminUser,HttpStatus.CREATED);
        }catch(Exception e){
            throw new RuntimeException("Error While Creating New User: "+e);
        }
    }

}
