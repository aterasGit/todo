package org.home.todo.util.exceptions;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}
