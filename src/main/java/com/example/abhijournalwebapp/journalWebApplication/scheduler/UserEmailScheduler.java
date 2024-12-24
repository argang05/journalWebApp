package com.example.abhijournalwebapp.journalWebApplication.scheduler;

import com.example.abhijournalwebapp.journalWebApplication.cache.AppCache;
import com.example.abhijournalwebapp.journalWebApplication.entity.JournalEntry;
import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.enums.Sentiment;
import com.example.abhijournalwebapp.journalWebApplication.model.SentimentData;
import com.example.abhijournalwebapp.journalWebApplication.repository.UserRepositoryImpl;
import com.example.abhijournalwebapp.journalWebApplication.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Here we're going to create a scheduling functionality to automate ending of email on regular interval of time
// or on specific time of a specific day of week.

@Component
public class UserEmailScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;


    @Autowired
    private AppCache appCache;

    //Using Kafka in the scheduling task to make task faster smooth and more efficient:
    @Autowired
    private KafkaTemplate<String , SentimentData> kafkaTemplate;

    //We are going to Schedule this Entire Function:
//    @Scheduled(cron = "0/30 * * ? * *")
    @Scheduled(cron = "0 0 9 * * SUN")
    //Cron Expression For Every Sunday Morning 9:00 am
    // (cron = "*sec* *min* *hour* *day_of_month month* *day_of_week*")
    public void fetchUsersAndSendSentimentAnalysisMail(){
        //Fetching all eligible users and sending them email through this method:
        List<User> eligibleUsers = userRepositoryImpl.getAllUsersForSentimentAnalysis();

        //Running forEach loop on eligibleUsers to target each user for sending email:
        for(User user : eligibleUsers){
            List<JournalEntry> journalEntriesList = user.getJournalEntries();

            //Filter journal entry content by date to fetch Sentiments Of Journal Entries of last 7 days:
            List<Sentiment> filteredSentiments = journalEntriesList.stream()
                    .filter(x -> x.getDate()
                            .isAfter(
                                    LocalDateTime.now().minus(7, ChronoUnit.DAYS)
                            )
                    ).map(x -> x.getSentiment())
                    .collect(Collectors.toList());

            //Count Sentiment and storing it in map:
            Map<Sentiment , Integer> sentimentCount = new HashMap<>();

            for(Sentiment sentiment : filteredSentiments){
                if(sentiment != null){
                    //Here we are picking each sentiment and trying to match whether we have
                    // similar sentiments if yes then increment the value the sentiment key or the sentimentCount
                    // (default value = 0)
                    sentimentCount.put(sentiment , sentimentCount.getOrDefault(sentiment,0)+ 1);
                }
            }
            //Calculating The Most Frequent Sentiment:
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment,Integer> sentimentCountEntry : sentimentCount.entrySet()){
                //Calculating maximum sentimentCount value and key associated with it:
                if(sentimentCountEntry.getValue() > maxCount){
                    maxCount = sentimentCountEntry.getValue();
                    mostFrequentSentiment = sentimentCountEntry.getKey();
                }
            }
            //Sending the most frequent sentiment of a user through email:
            if(mostFrequentSentiment != null){
                //We'll give the email sending job to kafka to produce it to the consumers through kafka brokers
                // or server for achieving distributed service functionality for fast, smooth and efficient
                // task execution:

                //Building SentimentData Instance:
                SentimentData sentimentData = SentimentData.builder()
                        .email(user.getEmail())
                        .sentiment("Sentiment Analysis Result For Last 7 days: "+mostFrequentSentiment)
                        .build();

                try{
                    //Using kafka to send email:
                    //We are passing three things:
                    // 1.) Topic Name We have Created To keep messages to be produced.
                    // 2.) Email address of each sentiment data to be used as key in partition based ordering.
                    // 3.) SentimentData object.
                    //We'll also handle kafka fallback (due to plan expiry) by using it synchronously
                    // using try catch:
                    kafkaTemplate.send(
                            "weekly-sentiments" ,
                            sentimentData.getEmail() ,
                            sentimentData
                    );
                }catch (Exception e){
                    //Using the emailService to send email if kafka fallback occurs
                    emailService.sendEmail(
                            sentimentData.getEmail(),
                            "Sentiment Analysis Result For Previous Week",
                            sentimentData.getSentiment()
                    );
                }

            }
        }
    }

    //Scheduling app cache clearance every 10 minutes automatically:
    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }
}
