package com.example.abhijournalwebapp.journalWebApplication.repository;
//For Writing Complex Query "Query Method DSL" is not a suitable choice. For that we're going
// to use Criteria API.

//We're Going to Create a functionality such that the user who has a valid email and
// has opted for sentimentAnalysis will get a weekly report of his/her sentiments (happy or sad etc.)
// through email (automated process) which will be evaluated from there journals
// using concept of NLP(Natural Language Processing)

import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {

    //MongoTemplate is used to provide a blueprint of the collections we are using in MongoDB:
    //It is a class provided by Spring Data MongoDB.
    //It helps us to run all the DDL and DML commands to query the DB.
    @Autowired
    private MongoTemplate mongoTemplate;

    //Get All Users Who have valid email and have opted for sentimentAnalysis using Criteria API:
    public List<User> getAllUsersForSentimentAnalysis(){
        //We're going to create complex queries and constraints(criteria) to filter our values:

        //Creating instance of Query class(Query and Criteria go hand-in-hand and is used together):
        Query query = new Query();

        //Adding Criteria in query to filter the eligible candidates for sentimentAnalysis:
        query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[A-Z|a-z]{2,6}$")); //Checking Valid Format Of Email
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

        //Passing the criteria query in mongoTemplate.find() method to filter and find all eligible users:
        List<User> eligibleUsers = mongoTemplate.find(query,User.class); //Provide Entity Reference
        return eligibleUsers;
    }

}
