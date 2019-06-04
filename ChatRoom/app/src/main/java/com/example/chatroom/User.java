package com.example.chatroom;

import java.io.Serializable;

public class User implements Serializable {

    String email;
    String password;
    String firstName;
    String lastName;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" + "email='" + email + '\'' + ", password='" + password + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + '}';
    }
}
