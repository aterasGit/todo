package org.home.todo.util.exceptions;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

}
