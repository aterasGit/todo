package org.home.todo.util;

import org.home.todo.models.User;
import org.home.todo.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Component
public class UserValidator implements Validator {

    private final UsersService usersService;

    @Autowired
    public UserValidator(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        User user = (User) target;
        if (usersService.getUser(user.getUsername()).isPresent())
            errors.rejectValue("username", "", "user already exists");
    }
}
