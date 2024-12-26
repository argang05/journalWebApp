package com.example.abhijournalwebapp.journalWebApplication.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//Tests For EmailService Component Here:

@Disabled
@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    @Disabled
    void sendEmailTests(){
        emailService.sendEmail("ganruly002@gmail.com","Testing Java Mail Sender","Juice Pila Do!! Mosambi Ka!!");
    }

}
