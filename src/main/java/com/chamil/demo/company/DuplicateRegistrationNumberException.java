package com.chamil.demo.company;

public class DuplicateRegistrationNumberException extends RuntimeException {

    public DuplicateRegistrationNumberException(String registrationNumber) {
        super("Registration number already exists: " + registrationNumber);
    }
}