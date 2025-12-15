package com.Learn.ELP_backend.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("USER_ALREADY_EXISTS");
    }
}
