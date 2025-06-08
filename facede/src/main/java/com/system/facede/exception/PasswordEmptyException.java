package com.system.facede.exception;

public class PasswordEmptyException extends RuntimeException {
    public PasswordEmptyException(String message) {
        super(message);
    }
}
