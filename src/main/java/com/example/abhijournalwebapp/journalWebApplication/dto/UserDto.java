package com.example.abhijournalwebapp.journalWebApplication.dto;

//User Data Transfer Object For Passing Only Relevant User Details During Signup:

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;
    private String email;
    private boolean sentimentAnalysis;
}
