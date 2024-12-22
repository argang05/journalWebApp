package com.example.abhijournalwebapp.journalWebApplication.service;

import com.example.abhijournalwebapp.journalWebApplication.api.response.QuotesResponse;
import com.example.abhijournalwebapp.journalWebApplication.cache.AppCache;
import com.example.abhijournalwebapp.journalWebApplication.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.util.Objects;

//Make this a IOC Bean using @Component annotation:
//@Component
//We can use @Service annotation instead of @Component for better readability
@Service
public class QuotesService {
    //Importing the value of API_KEY from yml config file using @Value annotation
    // To Avoid Exposing Sensitive API Keys:
    //Variable Should not be Static or Final.
    @Value("${quotes-api-key}")
    private String API_KEY;

    private static final String API_URL = "https://api.api-ninjas.com/v1/quotes?category=happiness";

    //We'll be using RestTemplate to send get request, set api-key in header and get response:

    //Create an instance:
    @Autowired
    private RestTemplate restTemplate;

    //Injecting AppCache Instance to use the config key:value pairs for api url:
    @Autowired
    private AppCache appCache;

    public QuotesResponse getQuotes(String category){
        //taking URL From the appCache in memory cache to avoid exposing it in public;
        String finalApiURL = appCache.appCacheMap.get(AppCache.keys.QUOTES_API_URL.toString())
                .replace(Placeholders.CATEGORY , category);

        //Set the headers:
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key",API_KEY);

        // Create an HttpEntity with the headers
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        //restTemplate.exchange(URL , REQUEST_METHOD , HEADERS , RESPONSE_TYPE)
        //Process Of Converting JSON Object into POJO (Plain Old Java Object) Is Called Deserialization.
        ResponseEntity<QuotesResponse[]> response = restTemplate.exchange(finalApiURL , HttpMethod.GET , requestEntity , QuotesResponse[].class);
        //POST Request : restTemplate.exchange(URL , Http.POST , REQUEST_ENTITY/BODY Of Type HttpEntity<>() , RESPONSE_TYPE)

        QuotesResponse responseDataBody = Objects.requireNonNull(response.getBody())[0];
        return responseDataBody;
    }
}
