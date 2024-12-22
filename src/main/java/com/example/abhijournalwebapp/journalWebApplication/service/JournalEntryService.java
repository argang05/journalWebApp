package com.example.abhijournalwebapp.journalWebApplication.service;

//Controller Will Call The Service and Service Will Call the Repository:

import com.example.abhijournalwebapp.journalWebApplication.entity.JournalEntry;
import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//Make this a IOC Bean using @Component annotation:
//@Component
//We can use @Service annotation instead of @Component for better readability
@Service
@Slf4j
public class JournalEntryService {

    //Using JournalEntryRepository in Service through concept of Dependency Injection:
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    //Saving a JournalEntry entity:
    //Making it a transaction using "@Transactional" annotation so that if later any sub operation fails then
    // the entire changes will be rolled back
    //The Entire function will be treated as a single transaction.
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        try{
            //Taking reference of User entity to save the ObjectId Of Journal Entity within User Entity
            User user = userService.findByUserName(userName);

            //Setting Date:
            journalEntry.setDate(LocalDateTime.now());
            //It will save an entry into the collection and if id is same it will automatically update the
            // existing entry
            //Retrieve the saved Journal Entry Reference
            JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntry);

            //Add The Id of Saved Journal Entity Reference in journalEntries field of User Entity:
            user.getJournalEntries().add(savedJournalEntry);

            //Save the updated user cred in db:
            userService.saveUser(user);
        }catch (Exception e){
            throw new RuntimeException("An Unexpected Error Occurred While Saving The Entry: ",e);
        }
    }

    //Overloaded Function with same name but different params used to just save journal Entry and not make
    // any changes in User entity:
    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    //Get all journal entries:
    public List<JournalEntry> getAllEntries(){
        //Using .findAll() method to get all the entries:
        return journalEntryRepository.findAll();
    }

    //Get a specific journal by Id:
    public Optional<JournalEntry> getEntryById(ObjectId journal_id){
        //Using .findById(id) method to get a specific journal entry:
        return journalEntryRepository.findById(journal_id);
    }

    //Delete a specific journal by Id and making it a transaction:
    @Transactional
    public boolean deleteEntryById(ObjectId journal_id, String userName){
        boolean isRemoved = false;
        try{
            User user = userService.findByUserName(userName);

            //Removing Reference of Journal Entry from User's Journal Entry Collection:
            isRemoved = user.getJournalEntries().removeIf(
                    (journalEntry) -> journalEntry.getId().equals(journal_id)
            );

            if(isRemoved){
                //Save Updated User Details:
                userService.saveUser(user);

                //Using .deleteById(id) method to delete a specific journal entry:
                journalEntryRepository.deleteById(journal_id);
            }
        }catch (Exception e){
            log.error("Error",e);
            throw new RuntimeException("Error occured while deleting an journal entry: "+e);
        }
        return isRemoved;
    }
}
