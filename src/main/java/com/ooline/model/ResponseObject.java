package com.ooline.model;

import org.springframework.http.HttpStatus;


public class ResponseObject {
    private String message;
    private HttpStatus status;


    public ResponseObject(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public ResponseObject getResponseObject(String message, HttpStatus status) {
        return new ResponseObject(message, status);
    }
}
