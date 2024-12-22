package com.example.abhijournalwebapp.journalWebApplication.cache;


//Application is a process of storing frequently used and frequently changing config
// data in database and load that db inside your spring-boot application.

import com.example.abhijournalwebapp.journalWebApplication.entity.ConfigJournalAppEntity;
import com.example.abhijournalwebapp.journalWebApplication.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    //Creating an enum for all keyNames:
    public enum keys{
        WEATHER_API_URL,
        QUOTES_API_URL
    }

    //Making A Hashmap for App Caching;
    public Map<String,String> appCacheMap;

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    //@PostConstruct annotation will immediately invoke the function associated with
    // it as soon as the bean gets created to reduce latency as much as feasible
    @PostConstruct
    public void init(){
        //Loading the entire collection into appCache at once and for once and this
        // appCache can be used as in-memory cache to store all configs and use them immediately
        // when called to reduce latency:

        //Reinitialize appCacheMap to avoid creating of duplicate keys:
        appCacheMap = new HashMap<>();

        List<ConfigJournalAppEntity> allConfigs = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity configJournalAppEntity : allConfigs){
            //Mapping to the collection and Storing all key:value pairs in hashmap:
            appCacheMap.put(configJournalAppEntity.getKey() , configJournalAppEntity.getValue());
        }

    }
}
