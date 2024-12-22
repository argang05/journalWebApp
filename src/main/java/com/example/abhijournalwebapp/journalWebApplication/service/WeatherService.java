package com.example.abhijournalwebapp.journalWebApplication.service;

import com.example.abhijournalwebapp.journalWebApplication.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//Make this a IOC Bean using @Component annotation:
//@Component
//We can use @Service annotation instead of @Component for better readability
@Service
public class WeatherService {
    //Importing the value of API_KEY from yml config file using @Value annotation
    // To Avoid Exposing Sensitive API Keys:
    //Variable Should not be Static or Final.
    @Value("${weather.api.key}")
    private String API_KEY;

    private static final String API_URL = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    //To Hit A External API From Spring Boot Code We Will Use RestTemplate:
    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeatherDetails(String city){
        String finalURL = API_URL.replace("CITY",city).replace("API_KEY",API_KEY);

        //restTemplate.exchange(URL , REQUEST_METHOD , HEADERS , RESPONSE_TYPE)
        //Process Of Converting JSON Object into POJO (Plain Old Java Object) Is Called Deserialization.
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalURL , HttpMethod.GET , null, WeatherResponse.class);
        //POST Request : restTemplate.exchange(URL , Http.POST , REQUEST_ENTITY/BODY Of Type HttpEntity , RESPONSE_TYPE)

        //Get the response dataBody:
        WeatherResponse dataBody = response.getBody();

        //Return body
        return dataBody;
    }


}
