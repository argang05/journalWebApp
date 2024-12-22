package com.example.abhijournalwebapp.journalWebApplication.service;


import org.springframework.stereotype.Service;

@Service
public class SentimentAnalysisService {

    public String getSentiment(String text){
        //This function will be used to analyze the text to send a sentiment evaluation result
        // as either Positive or Negative or Neutral:
        return "Sentiment : Happy!";
    }
}
