package org.home.todo.services;

import org.home.todo.models.User;
import org.home.todo.repositories.UserRepository;
import org.home.todo.security.UserDetails;
import org.home.todo.util.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Service
@Transactional(readOnly = true)
public class UsersService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) throw new UserNotFoundException("user not found");
        return new UserDetails(userOptional.get());
    }

    public Optional<User> getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.delete(((UserDetails) authentication.getPrincipal()).getUser());
    }

}
