package org.home.todo.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.home.todo.security.UserDetails;
import org.home.todo.services.UsersService;
import org.home.todo.util.JWTUtil;
import org.home.todo.util.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Component
public class JWTHttpFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UsersService usersService;

    @Autowired
    public JWTHttpFilter(JWTUtil jwtUtil, UsersService usersService) {
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null
                && authorizationHeader.startsWith("Bearer "))
            try {
                UserDetails userDetails = usersService.loadUserByUsername(
                        jwtUtil.validateJWTAndRetrieveUsername
                                    (authorizationHeader.substring(7))
                );
                SecurityContext securityContext = SecurityContextHolder.getContext();
                if (securityContext.getAuthentication() == null)
                    securityContext.setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    userDetails.getUsername(),
                                    userDetails.getAuthorities()
                            )
                    );
            } catch (JWTVerificationException | UserNotFoundException e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        filterChain.doFilter(request, response);
    }

}
