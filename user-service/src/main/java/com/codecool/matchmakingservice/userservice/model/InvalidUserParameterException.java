package com.codecool.matchmakingservice.userservice.model;

public class InvalidUserParameterException extends RuntimeException{
    public InvalidUserParameterException() {}

    public InvalidUserParameterException(String message) {
        super(message);
    }
}
