package com.dislinkt.agents.exceptions;

public class GenericException extends RuntimeException {

    private String message;

    public GenericException(String message) {
        super(message);
        this.message = message;

    }

}
