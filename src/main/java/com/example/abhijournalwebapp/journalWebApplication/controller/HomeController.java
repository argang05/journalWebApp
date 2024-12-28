package com.example.abhijournalwebapp.journalWebApplication.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//CrossOrigin Annotation Allows To Prevent Cors Error While Making Request From Frontend To Backend Due to Port Difference
@CrossOrigin
@RequestMapping("/api")

//We can give names to controller using @Tag annotation;
@Tag(name="Home APIs")
public class HomeController {
    @GetMapping("/")
    public String homeCall(){
        return "Home API Call";
    }
}
