package com.example.abhijournalwebapp.journalWebApplication.controller;

//Admin Controller For Defining API Calls For Admin Specific And Exclusive Tasks:

import com.example.abhijournalwebapp.journalWebApplication.cache.AppCache;
import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
//CrossOrigin Annotation Allows To Prevent Cors Error While Making Request From Frontend To Backend Due to Port Difference
@CrossOrigin
//We can give names to controller using @Tag annotation;
@Tag(name="Admin APIs", description = "APIs For Admin Specific Jobs or Tasks")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @GetMapping("/all-users")
    @Operation(summary = "Get All Users Details")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        if (allUsers != null && !allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create-new-admin")
    @Operation(summary = "Create A New Admin Entity")
    public ResponseEntity<?> createNewAdmin(@RequestBody User adminUser){
        try{
            userService.saveNewAdmin(adminUser);
            return new ResponseEntity<>(adminUser,HttpStatus.CREATED);
        }catch(Exception e){
            throw new RuntimeException("Error While Creating New User: "+e);
        }
    }

    //Adding and exposing an API Endpoint For Cleaning and Reinitialising appCache:
    @GetMapping("/clear-app-cache")
    public ResponseEntity<?> clearAppCache(){
        appCache.init();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
