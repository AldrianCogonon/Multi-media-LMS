package com.library.service;

import com.library.model.AdminUser;
import com.library.model.LibraryUser;
import com.library.model.User;

//This checks the username and password and returns the matching user.
public class AuthService {

    // POLYMORPHISM: the array type is User, so it holds an AdminUser
    // and a LibraryUser together.
    private User[] accounts = {
            new AdminUser("admin", "admin123"),
            new LibraryUser("libraryuser", "library123")
    };

    // Returns the logged-in user, or null if the username or password is wrong.
    public User login(String enteredUsername, String enteredPassword) {
        if (enteredUsername == null || enteredPassword == null) {
            return null;
        }

        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].checkLogin(enteredUsername, enteredPassword)) {
                return accounts[i];
            }
        }

        return null;
    }
}