package com.example.abhijournalwebapp.journalWebApplication.service;

//Service to interact with Redis.
//Our target is to save the entire WeatherResponse Entity in "in-memory cache" using Redis to save us an API call and time.

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    //Two Generic return-type methods to set and get value to and from Redis:

    public <T> T get(String key, Class<T> weatherResponseClass){
        try{
            Object obj = redisTemplate.opsForValue().get(key);
            if(obj != null){
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(obj.toString() , weatherResponseClass);
            }else{
                return null;
            }
        }catch (Exception e){
            log.error("Error Occurred While Getting Value: ",e);
            return null;
        }
    }

    public void set(String key, Object obj, Long timeToLive){
        try{
            //Converting POJO To JSON and then setting it:
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(obj);

            redisTemplate.opsForValue().set(key , jsonValue , timeToLive , TimeUnit.SECONDS);

        }catch (Exception e){
            log.error("Error Occurred While Getting Value: ",e);
        }
    }

}
