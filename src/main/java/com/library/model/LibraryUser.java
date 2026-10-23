package com.library.model;

// INHERITANCE: LibraryUser is a child of User.
// POLYMORPHISM (method overriding): the library user has different permissions
// from the admin. It can only view and search the media records, and borrow.
public class LibraryUser extends User {

    public LibraryUser(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "Library User";
    }

    @Override
    public boolean canManageItems() {
        return false;
    }

    @Override
    public boolean canBorrow() {
        return true;
    }

    @Override
    public boolean canManageBorrowing() {
        return false;
    }
}