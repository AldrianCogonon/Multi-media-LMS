package com.library.model;

// ABSTRACTION (abstract class): the general idea of "a person who logs in".
// It cannot be created with "new". Only AdminUser and LibraryUser can.
public abstract class User {

    // ENCAPSULATION: the password is private and has no getter.
    // Other classes can only ask checkLogin() whether a login is correct.
    private final String username;
    private final String password;

    public User(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        this.username = username.trim();
        this.password = password;
    }

    public boolean checkLogin(String enteredUsername, String enteredPassword) {
        if (enteredUsername == null || enteredPassword == null) {
            return false;
        }

        return username.equals(enteredUsername.trim())
                && password.equals(enteredPassword);
    }

    public String getUsername() {
        return username;
    }

    // ABSTRACTION: every child class MUST decide its own role and permissions.
    public abstract String getRole();

    // Add, update, delete items and calculate late fees.
    public abstract boolean canManageItems();

    // Borrow an item from the Collection page.
    public abstract boolean canBorrow();

    // See all borrow records and return items (the Borrowing tab).
    public abstract boolean canManageBorrowing();
}