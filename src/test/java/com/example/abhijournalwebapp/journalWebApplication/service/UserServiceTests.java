package com.example.abhijournalwebapp.journalWebApplication.service;

//We are gonna perform unit testing (Testing components or methods within code) using J-UNIT:

import com.example.abhijournalwebapp.journalWebApplication.entity.User;
import com.example.abhijournalwebapp.journalWebApplication.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Disabled
public class UserServiceTests {

    //A very basic test of adding two numbers for demonstration:
    //Use @Test annotation to depict that a particular function/method will perform a unit test:
//    @Test
//    public void addTwoTest(){
//        //assertEquals( expected_result , operation/method )
//        //It checks whether a specific operation is returning result equal to "expected_result":
//        assertEquals(4,2+2);
//    }

    @Autowired
    private UserRepository userRepository;

    //Adding a test to verify that findByUserName(String userName) method does not return null
    // when we enter an existing entity (eg: userName = Ram):
    //Such Tests will run properly only when the web app runs!
    // But that's not feasible as both things can't run at the same time so we
    // have use @SpringBootTest annotation on class to tackle this issue.
    @Test
//    //To Disable a test we can use @Disabled annotation:
//    @Disabled
    @Disabled
    public void testFindByUserName(){
        //assetNotNull() tests whether a particular method does not return null:
        //assert mean "Daava Karna".
        assertNotNull(userRepository.findByUserName("Ram"));
    }


    //Test whether the User has written some journals or not:
//Performing Parameterised Test Where inputs are not hard coded using @ParametrizedTest annotation:
    //To take input values for test we can use csvs for value inputs using @ValueSource
    //@CsvSource(strings = {"a,b,expected",...})
    @ParameterizedTest
    @ValueSource(strings = {
            "Ram",
            "Shyaam",
    })
    @Disabled
    public void testJournalEntriesOfUser(String userName){
        User user = userRepository.findByUserName(userName);
        assertTrue(!user.getJournalEntries().isEmpty() , "Test Failed For: "+userName);
    }


    //Performing Parameterised Test Where inputs are not hard coded using @ParametrizedTest annotation:
    //To take input values for test we can use csvs for value inputs using @CsvSource
    //@CsvSource({"a,b,expected",...})
    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,2,4",
            "5,5,10"
    })
    //To Disable a test we can use @Disabled annotation:
    @Disabled
    public void testAddTwo(int a,int b,int expected){
        assertEquals(expected,a+b);
    }
}
