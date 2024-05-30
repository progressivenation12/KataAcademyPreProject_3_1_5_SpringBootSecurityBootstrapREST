package ru.kata.spring.boot_security.demo.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.kata.spring.boot_security.demo.util.PersonErrorResponse;
import ru.kata.spring.boot_security.demo.util.PersonNotFoundException;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException exception) {
        PersonErrorResponse data = new PersonErrorResponse();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<PersonErrorResponse> handleException(Exception exception) {
        PersonErrorResponse data = new PersonErrorResponse();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
