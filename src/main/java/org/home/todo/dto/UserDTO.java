package org.home.todo.dto;

import org.springframework.hateoas.RepresentationModel;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

public class UserDTO extends RepresentationModel<UserDTO> {

    private int id;

    @Size(min = 4, max = 64, message = "4-64 characters required")
    @NotEmpty(message = "4-64 characters required")
    private String username;

    @Size(min = 4, max = 128, message = "4-128 characters required")
    @NotEmpty(message = "4-128 characters required")
    private String password;

    private String jwt;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
