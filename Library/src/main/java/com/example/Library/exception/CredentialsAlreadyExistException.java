package com.example.Library.exception;

public class CredentialsAlreadyExistException extends RuntimeException{
    public CredentialsAlreadyExistException(String message) {
        super(message);
    }
}
