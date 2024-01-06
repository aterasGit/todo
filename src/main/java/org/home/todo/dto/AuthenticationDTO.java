package org.home.todo.dto;

import javax.validation.constraints.Size;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

public class AuthenticationDTO {

    @Size(min = 4, max = 64, message = "4-64 characters required")
    private String username;

    @Size(min = 4, max = 128, message = "4-128 characters required")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
