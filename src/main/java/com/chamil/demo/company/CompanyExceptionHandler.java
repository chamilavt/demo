package com.chamil.demo.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CompanyExceptionHandler {

    @ExceptionHandler(DuplicateRegistrationNumberException.class)
    public ResponseEntity<String> handleDuplicateRegistrationNumber(
            DuplicateRegistrationNumberException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidRegistrationNumberException.class)
    public ResponseEntity<String> handleInvalidRegistrationNumber(
            InvalidRegistrationNumberException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}package com.chamil.demo.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CompanyExceptionHandler {

    @ExceptionHandler(DuplicateRegistrationNumberException.class)
    public ResponseEntity<String> handleDuplicateRegistrationNumber(
            DuplicateRegistrationNumberException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidRegistrationNumberException.class)
    public ResponseEntity<String> handleInvalidRegistrationNumber(
            InvalidRegistrationNumberException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}