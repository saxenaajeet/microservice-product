package com.ooline.exceptions;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
