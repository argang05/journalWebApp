package com.example.abhijournalwebapp.journalWebApplication.service;

import com.example.abhijournalwebapp.journalWebApplication.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    @Autowired
    private EmailService emailService;

    //Creating a KafkaListener For Listening To Incoming Requirement to Send Email (which is scheduled).
    //As soon as requirement comes sendEmail() will be called and email will be sent:
    @KafkaListener(topics = "weekly-sentiments" , groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentimentData){
        sendEmail(sentimentData);
    }

    private void sendEmail(SentimentData sentimentData){
        emailService.sendEmail(
                sentimentData.getEmail(),
                "Sentiment Analysis Result For Previous Week",
                sentimentData.getSentiment()
        );
    }
}
