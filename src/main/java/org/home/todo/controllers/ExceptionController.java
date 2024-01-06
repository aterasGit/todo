package org.home.todo.controllers;

import org.home.todo.util.exceptions.TaskNotFoundException;
import org.home.todo.util.exceptions.EntityRejectedException;
import org.home.todo.util.exceptions.UnauthenticatedException;
import org.home.todo.util.exceptions.UserNotFoundException;
import org.home.todo.util.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UnauthenticatedException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage(), System.currentTimeMillis()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(EntityRejectedException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(TaskNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), System.currentTimeMillis()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), System.currentTimeMillis()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleException(MissingRequestHeaderException e) {
        return new ResponseEntity<>(
                new ErrorResponse("unauthorized", System.currentTimeMillis()),
                HttpStatus.UNAUTHORIZED
        );
    }

}

