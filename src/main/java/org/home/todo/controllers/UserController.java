package org.home.todo.controllers;

import org.home.todo.dto.AuthenticationDTO;
import org.home.todo.dto.UserDTO;
import org.home.todo.models.User;
import org.home.todo.security.UserDetails;
import org.home.todo.services.UsersService;
import org.home.todo.util.CheckUtil;
import org.home.todo.util.JWTUtil;
import org.home.todo.util.UserValidator;
import org.home.todo.util.exceptions.UnauthenticatedException;
import org.home.todo.util.responses.GenericResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@RestController
@RequestMapping({"/user", "/"})
public class UserController {

    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final UsersService usersService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CheckUtil checkUtil;

    @Autowired
    public UserController(ModelMapper modelMapper, UserValidator userValidator, UsersService usersService,
                          JWTUtil jwtUtil, AuthenticationManager authenticationManager, CheckUtil checkUtil) {
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.usersService = usersService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.checkUtil = checkUtil;
    }

    @PostMapping("/add")
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        User user = userDTO2User(userDTO);
        userValidator.validate(user, bindingResult);
        checkUtil.checkForValidationErrors(bindingResult);
        usersService.addUser(user);
        userDTO = user2UserDTO(user);
        userDTO.setJwt(jwtUtil.generateJWT(user.getUsername()));
        return new ResponseEntity<>(addLinks(userDTO), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<GenericResponse> deleteUser() {
        usersService.deleteUser();
        return new ResponseEntity<>(
                new GenericResponse("user deleted", System.currentTimeMillis()),
                HttpStatus.OK
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody @Valid AuthenticationDTO authenticationDTO,
                                             BindingResult bindingResult) {
        checkUtil.checkForValidationErrors(bindingResult);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationDTO.getUsername(), authenticationDTO.getPassword())
                    );
        } catch (AuthenticationException e) {
            throw new UnauthenticatedException("unauthenticated");
        }
        UserDTO userDTO = user2UserDTO(((UserDetails) authentication.getPrincipal()).getUser());
        userDTO.setJwt(jwtUtil.generateJWT(userDTO.getUsername()));
        return new ResponseEntity<>(
                addLinks(userDTO),
                HttpStatus.OK
        );
    }

    private User userDTO2User(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO user2UserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private UserDTO addLinks(UserDTO userDTO) {
        userDTO.add(
                linkTo(UserController.class)
                    .slash("add")
                    .withRel("addUser")
                    .withTitle("add new user and get a new jwt for authentication")
        );
        userDTO.add(
                linkTo(UserController.class)
                    .slash("login")
                    .withRel("loginUser")
                    .withTitle("get a new jwt for authentication")
        );
        userDTO.add(
                linkTo(UserController.class)
                    .slash("delete")
                    .withRel("deleteUser")
                    .withTitle("delete authenticated user")
        );
        return userDTO;
    }

}
