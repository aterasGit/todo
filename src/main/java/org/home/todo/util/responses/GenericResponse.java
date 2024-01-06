package org.home.todo.util.responses;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

public class GenericResponse {

    private String message;
    private long timestamp;

    public GenericResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
