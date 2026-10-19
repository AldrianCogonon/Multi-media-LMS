package com.library.controller;

import com.library.service.AuthService;

public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    public boolean login(String username, String password) {
        return authService.login(username, password);
    }
}