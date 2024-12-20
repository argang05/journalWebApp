package com.example.abhijournalwebapp.journalWebApplication.repository;

//Spring Data MongoDB provides a interface called MongoRepository which enables us to query
// mongodb database by providing predefined methods for that purpose:

import com.example.abhijournalwebapp.journalWebApplication.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface JournalEntryRepository extends MongoRepository <JournalEntry , ObjectId>{
    //JournalEntry is data-type for specifying the entity type and ObjectId is data-type for the Id(Primary Key).
}
