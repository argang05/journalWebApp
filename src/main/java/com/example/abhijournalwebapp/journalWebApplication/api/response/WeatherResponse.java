package com.example.abhijournalwebapp.journalWebApplication.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

//Process Of Converting JSON Object into POJO (Plain Old Java Object) Is Called Deserialization.

@Getter
@Setter
public class WeatherResponse {
    public Current current;
    @Getter
    @Setter
    public class Current{
        private int temperature;
        //We're changing variable name from snake_case to camelCase but we have to mention how it
        // looked in response body
        @JsonProperty("weather_descriptions")
        private ArrayList<String> weatherDescriptions;
        private int feelslike;
    }
}

