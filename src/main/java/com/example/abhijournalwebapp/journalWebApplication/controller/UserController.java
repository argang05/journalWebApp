package com.example.abhijournalwebapp.journalWebApplication.controller;

import com.example.abhijournalwebapp.journalWebApplication.api.response.QuotesResponse;
import com.example.abhijournalwebapp.journalWebApplication.api.response.WeatherResponse;
import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.repository.UserRepository;
import com.example.abhijournalwebapp.journalWebApplication.service.QuotesService;
import com.example.abhijournalwebapp.journalWebApplication.service.UserService;
import com.example.abhijournalwebapp.journalWebApplication.service.WeatherService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

//Controllers are basically special type of components that handle multiple http requests.

//Setting A Context Parameter using @RequestMapping() as "/journal".
//It will add a mapping on the class and not method.
//Now to access any endpoint urls within this controller, complete url will look like:
// localhost:8080/journal/api_endpoint_name
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    //Inject WeatherService and QuotesService:
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private QuotesService quotesService;

    @GetMapping("/id/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable ObjectId userId){
        User user = userService.getUserById(userId);

        if(user != null){
            return new ResponseEntity<>(user , HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping
    public ResponseEntity<User> updateUserCredentials(@RequestBody User newUserCred){

        //We're going to fetch the user credentials(Username) Using security context holder:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User oldUserCred = userService.findByUserName(userName);

        //Update Credentials:
        oldUserCred.setUserName(newUserCred.getUserName());
        oldUserCred.setPassword(newUserCred.getPassword());

        //Save the user:
        userService.saveNewUser(oldUserCred);

        return new ResponseEntity<>(oldUserCred , HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById(){
        try{
            //We're going to fetch the user credentials(Username) Using security context holder:
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            userRepository.deleteByUserName(userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/greet-user")
    public ResponseEntity<?> greeting(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        WeatherResponse weatherResponse = weatherService.getWeatherDetails("Mumbai");

        QuotesResponse quotesResponse = quotesService.getQuotes("happiness");

        String greetingMessage = "Hi! "+userName;

        if(weatherResponse != null && quotesResponse != null){
            int feelsLikeTemp = weatherResponse.getCurrent().getFeelslike();
            String quote = "'"+quotesResponse.getQuote()+"'";
            greetingMessage += "\nWeather Feels Like: "+feelsLikeTemp+" deg.C"+
                    "\nQuote For Today: "+quote;
        }
        return new ResponseEntity<>(greetingMessage,HttpStatus.OK);
    }
}
