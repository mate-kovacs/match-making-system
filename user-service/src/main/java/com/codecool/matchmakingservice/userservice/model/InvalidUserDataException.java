package com.codecool.matchmakingservice.userservice.model;

public class InvalidUserDataException extends RuntimeException{
    public InvalidUserDataException() {}

    public InvalidUserDataException(String message) {
        super(message);
    }
}
