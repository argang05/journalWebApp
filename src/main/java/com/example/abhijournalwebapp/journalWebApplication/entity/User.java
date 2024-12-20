package com.example.abhijournalwebapp.journalWebApplication.entity;

/*
-> Creating User Entity For Journal WebApp.
-> Each User will have multiple journals associated to him/her.
-> We have to use concept of MongoDB Relationships in order to establish a relationship between an User
entity and the journals associated to him/her.
-> Structure:
1.) "Username" String.
2.) "Password" String (But Encoded).
3.) "JournalEntries" List (Containing ObjectIds Of all associated journals).
 */

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User {
    //We use @Id annotation to map id to the _id of mongodb entity
    @Id
    private ObjectId id;

    //Indexing Does not happen through this command for this we have to write a config command in application.properties:
    @Indexed(unique = true)
    @NonNull
    private String userName; //Making Username Unique and Not Null

    @NonNull
    private String password;

    private List<String> roles;

    //Using @DBRef to establish relationship/reference to the JournalEntry Entity:
    //It will store a list of references of JournalEntry entities which will contain the ObjectIds.
    //It will act as a foreign key where if any JournalEntry entity gets deleted from the collection,
    // the reference will get automatically deleted from users entity and vice versa:
    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();
}
