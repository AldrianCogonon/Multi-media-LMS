package com.library.service;
//This checks the username and password.
public class AuthService {

    private String username = "libraryuser";
    private String password = "library123";

    public boolean login(String enteredUsername, String enteredPassword) {
        if (enteredUsername == null || enteredPassword == null) {
            return false;
        }

        if (username.equals(enteredUsername.trim())
                && password.equals(enteredPassword)) {
            return true;
        } else {
            return false;
        }
    }
}
