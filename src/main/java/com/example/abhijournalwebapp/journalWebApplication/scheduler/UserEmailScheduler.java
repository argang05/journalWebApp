package com.example.abhijournalwebapp.journalWebApplication.scheduler;

import com.example.abhijournalwebapp.journalWebApplication.cache.AppCache;
import com.example.abhijournalwebapp.journalWebApplication.entity.JournalEntry;
import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.repository.UserRepositoryImpl;
import com.example.abhijournalwebapp.journalWebApplication.service.EmailService;
import com.example.abhijournalwebapp.journalWebApplication.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private AppCache appCache;

    //We are going to Schedule this Entire Function:
    @Scheduled(cron = "0 0 9 * * SUN")
    //Cron Expression For Every Sunday Morning 9:00 am
    // (cron = "*sec* *min* *hour* *day_of_month month* *day_of_week*")
    public void fetchUsersAndSendSentimentAnalysisMail(){
        //Fetching all eligible users and sending them email through this method:
        List<User> eligibleUsers = userRepositoryImpl.getAllUsersForSentimentAnalysis();

        //Running forEach loop on eligibleUsers to target each user for sending email:
        for(User user : eligibleUsers){
            List<JournalEntry> journalEntriesList = user.getJournalEntries();

            //Filter journal entry content by date to fetch journal entries of last 7 days:
            List<String> filteredJournalEntryContent = journalEntriesList.stream()
                    .filter(x -> x.getDate()
                            .isAfter(
                                    LocalDateTime.now().minus(7, ChronoUnit.DAYS)
                            )
                    ).map(x -> x.getContent())
                    .collect(Collectors.toList());

            //Joining all journal entry content of last 7 days into one String variable:
            String joinedJournalEntryContent = String.join(" ",filteredJournalEntryContent);

            //Passing the String of content and receiving Sentiment:
            String sentimentEvaluationResult = sentimentAnalysisService.getSentiment(joinedJournalEntryContent);

            //Sending The Email containing sentimentEvaluationResult:
            emailService.sendEmail(
                    user.getEmail(),
                    "Sentiment Evaluation Result For Last 7 Days",
                    sentimentEvaluationResult
            );
        }
    }

    //Scheduling app cache clearance every 10 minutes automatically:
    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }
}
