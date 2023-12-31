package org.home.todo.security;

import org.home.todo.models.User;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private User user;

    public UserDetails() {}

    public UserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
