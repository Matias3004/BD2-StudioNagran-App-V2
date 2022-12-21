package com.studionagranapp.helpers.models;

public class User {

    private final Integer accountId;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final String role;
    private final String email;
    private final String phoneNumber;

    public User(Integer id, String firstName, String lastName,
                String username, String password, String role,
                String email, String phoneNumber) {
        this.accountId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
