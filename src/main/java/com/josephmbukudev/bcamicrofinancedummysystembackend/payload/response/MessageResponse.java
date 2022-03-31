package com.josephmbukudev.bcamicrofinancedummysystembackend.payload.response;

public class MessageResponse {
    private String message;

//    Parameterized Constructor
    public MessageResponse(String message) {
        this.message = message;
    }

//    Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
