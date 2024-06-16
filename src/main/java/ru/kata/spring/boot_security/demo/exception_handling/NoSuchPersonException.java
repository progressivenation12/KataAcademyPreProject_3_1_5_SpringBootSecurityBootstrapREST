package ru.kata.spring.boot_security.demo.exception_handling;

public class NoSuchPersonException extends RuntimeException {

    public NoSuchPersonException(String message) {
        super(message);
    }
}
