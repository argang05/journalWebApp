package com.example.abhijournalwebapp.journalWebApplication.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

//Using @Document(collection = "collection_name") annotation for achieving Object Relational Mapping
// for mapping to MongoDB Database:
@Document(collection = "config_journal_app")

//We're gonna use lombok to simplify our code and reduce the need of writing multiple getters and setters;
@Data
@NoArgsConstructor
public class ConfigJournalAppEntity {
    private String key;
    private String value;
}
