
package com.library.service;

public class AuthService {

    // Temporary library user credentials stored in memory.
    private final String libraryUsername = "libraryuser";
    private final String libraryPassword = "library123";

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        return libraryUsername.equals(username.trim())
                && libraryPassword.equals(password);
    }
}
