package com.example.abhijournalwebapp.journalWebApplication.repository;

//Spring Data MongoDB provides a interface called MongoRepository which enables us to query
// mongodb database by providing predefined methods for that purpose:

import com.example.abhijournalwebapp.journalWebApplication.entity.ConfigJournalAppEntity;
import com.example.abhijournalwebapp.journalWebApplication.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ConfigJournalAppRepository extends MongoRepository <ConfigJournalAppEntity, ObjectId>{
}
