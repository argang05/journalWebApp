package com.example.abhijournalwebapp.journalWebApplication.service;


//We're creating an Email Service which will be used to send emails automatically to users opted for Sentiment Analysis.

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    //There is an Interface called JavaMailSender which helps developers to
    // automate process sending email to specific email ids:
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body){
        try{
            //Instantiate SimpleMailMessage Class:
            SimpleMailMessage mail = new SimpleMailMessage();

            //Setting Mail Properties(to,subject,body):
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);

            //Send The Mail:
            javaMailSender.send(mail);

        }catch (Exception e){
            log.error("Error Sending Email: ",e);
        }
    }


}
