package com.example.abhijournalwebapp.journalWebApplication.repository;

//Spring Data MongoDB provides a interface called MongoRepository which enables us to query
// mongodb database by providing predefined methods for that purpose:

import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository <User, ObjectId>{
    //JournalEntry is data-type for specifying the entity type and ObjectId is data-type for the Id(Primary Key).

    //Declaring an custom function to return user entity as per the username:
    //Query Method DSL : Writing Method name in such a way that it becomes a query.
    //For Writing Complex Query "Query Method DSL" is not a suitable choice. For that we're going to use Criteria API.
    User findByUserName(String userName);

    void deleteByUserName(String userName);
}
