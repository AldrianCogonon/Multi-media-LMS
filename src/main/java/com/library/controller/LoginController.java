package com.library.controller;

import com.library.model.User;
import com.library.service.AuthService;

public class LoginController {

    private AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    // Returns the logged-in user, or null if the login failed.
    public User login(String username, String password) {
        return authService.login(username, password);
    }
}