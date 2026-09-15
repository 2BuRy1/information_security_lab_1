package com.security.lab.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("Password and repeatedPassword do not match");
    }
}
