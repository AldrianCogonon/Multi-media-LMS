package com.library.model;

// INHERITANCE: AdminUser is a child of User.
// POLYMORPHISM (method overriding): the admin gets its own permissions.
// The admin manages the library, but does not borrow items.
public class AdminUser extends User {

    public AdminUser(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    @Override
    public boolean canManageItems() {
        return true;
    }

    @Override
    public boolean canBorrow() {
        return false;
    }

    @Override
    public boolean canManageBorrowing() {
        return true;
    }
}