package com.example.abhijournalwebapp.journalWebApplication.entity;

import com.example.abhijournalwebapp.journalWebApplication.enums.Sentiment;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

//Using @Document(collection = "collection_name") annotation for achieving Object Relational Mapping
// for mapping to MongoDB Database:
@Document(collection = "journal_entries")

//We're gonna use lombok to simplify our code and reduce the need of writing multiple getters and setters;
@Data
@NoArgsConstructor
public class JournalEntry {
    /*
    JournalEntry class will basically be used to create template of a new entry or
    journal entity with necessary details like journal's id, title, content.
     */

    //@Id annotation indicates that the particular field is to be treated as Primary Key
    //We use @Id annotation to map id to the _id of mongodb entity
    @Id
    private ObjectId id;

    @NonNull
    private String title; //Making title field NOT NULL

    private String content;
    private LocalDateTime date;

    private Sentiment sentiment;
}
