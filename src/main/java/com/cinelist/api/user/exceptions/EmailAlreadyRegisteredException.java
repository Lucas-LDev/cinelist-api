package com.cinelist.api.user.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String msg) {
        super(msg);
    }
}
