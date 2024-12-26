package com.example.abhijournalwebapp.journalWebApplication.controller;

import com.example.abhijournalwebapp.journalWebApplication.entity.JournalEntry;
import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.service.JournalEntryService;
import com.example.abhijournalwebapp.journalWebApplication.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

//Controllers are basically special type of components that handle multiple http requests.

//Setting A Context Parameter using @RequestMapping() as "/journal".
//It will add a mapping on the class and not method.
//Now to access any endpoint urls within this controller, complete url will look like:
// localhost:8080/journal/api_endpoint_name
@RestController
@RequestMapping("/api/journal")
public class JournalEntryController {
    //Note: Make Sure All Methods Inside A Controller are public so that it can be accessed by all users!

    //Using JournalEntryService in JournalEntryController Using Dependency Injection Concept:
    @Autowired
    private JournalEntryService journalEntryService;

    //We're also going to inject UserService So That We Can Fetch and Manipulate User Specific Journal Entities:
    @Autowired
    private UserService userService;

    //GET API Endpoint to GET All Journal Entries:
    //ResponseEntity<Type>() is used to enable developers send Http Status Codes along with response body.
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(){// localhost:8080/journal GET
        //Fetching Username from SecurityContextHolder instead of passing it through path variable:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.findByUserName(userName);

        //get All User Specific Journal Entries:
        List<JournalEntry> allEntries = user.getJournalEntries();
        if(allEntries != null && !allEntries.isEmpty()){
            return new ResponseEntity<>(allEntries,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //POST API endpoint to Create A New Entry:
    //-> We have to Send Data As Request Body (using @RequestBody annotation) just like how we
    // used to send request body in node.js:
    //-> It takes data from request and turns it into a java object so that it can be used in code easily
    //-> The request body will be of type "JournalEntry" Class
    @PostMapping
    public ResponseEntity<JournalEntry> createNewEntry(@RequestBody JournalEntry myEntry){//localhost:8080/journal POST

        try{
            //Fetching Username from SecurityContextHolder instead of passing it through path variable:
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            //Use the saveEntry() method of our journalEntryService to save a new entry:
            journalEntryService.saveEntry(myEntry , userName);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    //GET API Request to Fetch Specific Journal Entity Using A Unique Id and Path Variable:
    //if url is localhost:8080/jornal/id/2 then 2 is our path variable:
    //We can use @PathVariable Annotation to Access Path Variable within function (dtype Long as in hashmap):
    @GetMapping("/id/{journalId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId journalId){
        //Fetching Username from SecurityContextHolder instead of passing it through path variable:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.findByUserName(userName);

        //Collect User's All Relevant Journal Based On the JournalEntry ObjectId Refernce Using Filter:
        List<JournalEntry> relevantJournalEntryCollection = user.getJournalEntries()
                .stream()
                .filter(x -> x.getId().equals(journalId))
                .collect(Collectors.toList());

        if(!relevantJournalEntryCollection.isEmpty()){
            //Using the getEntryById(Id) method defined in journalEntryService Class:
            Optional<JournalEntry> journalEntry = journalEntryService.getEntryById(journalId);
            if(journalEntry.isPresent()){
                //If journal entry is present then send data with appropriate status code:
                return new ResponseEntity<>(journalEntry.get() , HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //DELETE API Request To Delete a Specific Journal Entity Using A Unique Id and Path Variable:
    @DeleteMapping("/id/{journalId}")
    public ResponseEntity<?> deleteJournalEntityById(@PathVariable ObjectId journalId){
        //Using the deleteEntryById(Id) method defined in journalEntryService Class:
        //Pass userName in the function so that we can also delete the reference of journal entry from
        // user's journal entry collections:

        //Fetching Username from SecurityContextHolder instead of passing it through path variable:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        //Checking status of deletion as same entity cannot be deleted again and again:
        boolean isRemoved = journalEntryService.deleteEntryById(journalId , userName);

        if(isRemoved){
            //NO_CONTENT Is Used To Indicate Successful Deletion Of Data:
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //PUT API Request To Update a Specific Journal Entity Using A Unique Id and Path Variable:
    @PutMapping("/id/{journalId}")
    public ResponseEntity<JournalEntry> updateJournalEntityById(
            @PathVariable ObjectId journalId ,
            @RequestBody JournalEntry updatedRequestEntry
    ){
        //Fetching Username from SecurityContextHolder instead of passing it through path variable:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.findByUserName(userName);

        //Collect User's All Relevant Journal Based On the JournalEntry ObjectId Refernce Using Filter:
        List<JournalEntry> relevantJournalEntryCollection = user.getJournalEntries()
                .stream()
                .filter(x -> x.getId().equals(journalId))
                .collect(Collectors.toList());

        if(!relevantJournalEntryCollection.isEmpty()){
            //Get The Specific Entry:
            JournalEntry oldJournalEntry = journalEntryService.getEntryById(journalId).orElse(null);

            if(oldJournalEntry != null){
                //Conditional Updation Of Old Journal Entry With the New One:
                oldJournalEntry.setTitle(
                        (updatedRequestEntry.getTitle() != null && !updatedRequestEntry.getTitle().equals(""))
                                ? updatedRequestEntry.getTitle() : oldJournalEntry.getTitle()
                );

                oldJournalEntry.setContent(
                        (updatedRequestEntry.getContent() != null && !updatedRequestEntry.getContent().equals(""))
                                ? updatedRequestEntry.getContent() : oldJournalEntry.getContent()
                );
                //Use the saveEntry() method of our journalEntryService to save a new entry:
                journalEntryService.saveEntry(oldJournalEntry);
                return new ResponseEntity<>(oldJournalEntry,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
