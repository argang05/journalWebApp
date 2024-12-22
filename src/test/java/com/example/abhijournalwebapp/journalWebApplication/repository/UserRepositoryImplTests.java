package com.example.abhijournalwebapp.journalWebApplication.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//Testing UserRepositoryImpl Methods Here:

@SpringBootTest
public class UserRepositoryImplTests {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    public void getAllUserForSentimentAnalysisTest(){
        Assertions.assertNotNull(userRepositoryImpl.getAllUsersForSentimentAnalysis());
    }
}