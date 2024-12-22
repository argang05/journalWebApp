package com.example.abhijournalwebapp.journalWebApplication.service;

import com.example.abhijournalwebapp.journalWebApplication.api.response.WeatherResponse;
import com.example.abhijournalwebapp.journalWebApplication.cache.AppCache;
import com.example.abhijournalwebapp.journalWebApplication.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    private String apiKey;

    //To Hit A External API From Spring Boot Code We Will Use RestTemplate:
    @Autowired
    private RestTemplate restTemplate;

    //Injecting AppCache Instance to use the config key:value pairs for api url:
    @Autowired
    private AppCache appCache;

    public WeatherResponse getWeatherDetails(String city){
        //taking URL From the appCache in memory cache to avoid exposing it in public
        String finalURL = appCache.appCacheMap.get(AppCache.keys.WEATHER_API_URL.toString())
                .replace(Placeholders.CITY, city) //Instead Of Hardcoding Storing Placeholder mapping in separate interface
                .replace(Placeholders.API_KEY, apiKey);

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
