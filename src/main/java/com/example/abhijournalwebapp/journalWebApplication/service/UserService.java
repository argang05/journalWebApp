package com.example.abhijournalwebapp.journalWebApplication.service;

//Controller Will Call The Service and Service Will Call the Repository:
//Making All Necessary Database Querying Utility Functions / Services to be Used in Controller:

import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

//Make this a IOC Bean using @Component annotation:
//@Component
//We can use @Service annotation instead of @Component for better readability
@Service
//We can avoid creating logger instances using @Slf4j annotation on Class:
@Slf4j
public class UserService {

    //Using UserRepository in Service through concept of Dependency Injection:
    @Autowired
    private UserRepository userRepository;

    //Using LogBack For Achieving Custom Logging Of Errors And Other Types Of Messages:

    //Create a Logger Instance (From slf4j Package) associated with UserService Class:
    //We want it to be private, avoid re-instantiation in UserService using static and
    // avoid accidental re-assignment using final keyword(s):
//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    //We can avoid creating instances using @Slf4j annotation on Class:

    //Using Bcrypt Encoder to Encode our password:

    //Creating a BCryptPasswordEncoder Instance:
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUser(User user) {
        if (user.getUserName().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        userRepository.save(user);
    }

    public void saveNewUser(User user) {
        try{
            if (user.getUserName().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty.");
            }
            //encoding password and setting it back to the field:
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            //Setting a Default user role:
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
        }catch(Exception e){
            //We're going to use LogBack Package To Log Any Errors Occurred Here;
//            logger.error("An Unexpected Error Occurred While Saving The User named {}: ",user.getUserName(),e);
            log.error("An Unexpected Error Occurred While Saving The User named {}: ",user.getUserName(),e);
        }
    }

    public void saveNewAdmin(User user) {
        try{
            if (user.getUserName().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty.");
            }
            //encoding password and setting it back to the field:
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            //Setting a Default user role:
            user.setRoles(Arrays.asList("USER","ADMIN"));
            userRepository.save(user);
        }catch(Exception e){
            //We're going to use LogBack Package To Log Any Errors Occurred Here;
            log.debug("An Unexpected Error Occurred While Saving The Admin with name {}: ",user.getUserName(),e);
        }
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(ObjectId userId){
        return userRepository.findById(userId).orElse(null);
    }

    public boolean deleteUserById(ObjectId userId){
        userRepository.deleteById(userId);
        return true;
    }

    public User findByUserName(String userName){
        //Calling the custom function .findByUsername(userName):
        return userRepository.findByUserName(userName);
    }


}
