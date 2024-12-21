package com.example.abhijournalwebapp.journalWebApplication.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuotesResponse {
    private String quote;
    private String author;
    private String category;
}
