package com.example.abhijournalwebapp.journalWebApplication.service;

import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

//We're gonna implement the interface UserDetailsService in order to get access to its method loadUserByUsername

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    //We'll retrieve the user and build user credentials for authentication and authorisation:

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Find User From DB By Username:
        User user = userRepository.findByUserName(username);

        if(user != null){
            //Building User Credential Entity:
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0])) //Array Of String representing roles
                    .build();

            return userDetails;
        }else {
            throw new UsernameNotFoundException("User not found with username: "+username);
        }
    }
}
