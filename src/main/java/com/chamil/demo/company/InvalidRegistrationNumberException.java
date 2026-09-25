package com.chamil.demo.company;

public class InvalidRegistrationNumberException extends RuntimeException {

    public InvalidRegistrationNumberException() {
        super("Registration number must use the format XX-123456");
    }
}