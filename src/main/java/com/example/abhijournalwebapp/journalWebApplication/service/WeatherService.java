package com.example.abhijournalwebapp.journalWebApplication.service;

import com.example.abhijournalwebapp.journalWebApplication.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    private static final String API_KEY = "d5dd1ecbe869b5afa7b354f20d1bb84a";
    private static final String API_URL = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    //To Hit A External API From Spring Boot Code We Will Use RestTemplate:
    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeatherDetails(String city){
        String finalURL = API_URL.replace("CITY",city).replace("API_KEY",API_KEY);

        //restTemplate.exchange(URL , REQUEST_METHOD , HEADERS , RESPONSE_TYPE)
        //Process Of Converting JSON Object into POJO (Plain Old Java Object) Is Called Deserialization.
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalURL , HttpMethod.GET , null, WeatherResponse.class);

        //Get the response dataBody:
        WeatherResponse dataBody = response.getBody();

        //Return body
        return dataBody;
    }


}