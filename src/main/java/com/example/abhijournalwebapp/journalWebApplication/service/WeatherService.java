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

    //Using RedisService To Set and Get Values to and from In Memory Cache
    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeatherDetails(String city){
        //Get the weatherResponse Data From In Redis InMemory Cache
        WeatherResponse weatherResponse = redisService.get("weather_of_"+city, WeatherResponse.class);
        if(weatherResponse != null){
            //if data is present in cache then return it
            return weatherResponse;
        }else {
            //Otherwise fetch from api and then set in redis IMC:

            //taking URL From the appCache in memory cache to avoid exposing it in public
            String finalURL = appCache.appCacheMap.get(AppCache.keys.WEATHER_API_URL.toString())
                    .replace(Placeholders.CITY, city) //Instead Of Hardcoding Storing Placeholder mapping in separate interface
                    .replace(Placeholders.API_KEY, apiKey);

            //restTemplate.exchange(URL , REQUEST_METHOD , HEADERS , RESPONSE_TYPE)
            //Process Of Converting JSON Object into POJO (Plain Old Java Object) Is Called Deserialization.
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalURL, HttpMethod.GET, null, WeatherResponse.class);
            //POST Request : restTemplate.exchange(URL , Http.POST , REQUEST_ENTITY/BODY Of Type HttpEntity , RESPONSE_TYPE)

            //Get the response dataBody:
            WeatherResponse dataBody = response.getBody();

            //Save the dataBody in Redis In-Memory Cache;
            if(dataBody != null){
                redisService.set("weather_of_"+city , dataBody , 300l); //300 seconds = 5 min
            }
            //Return body
            return dataBody;
        }
    }


}
