package com.example.interviewcoach.service;

public class NoQuestionsAvailableException extends RuntimeException {

    public NoQuestionsAvailableException(String message) {
        super(message);
    }
}
