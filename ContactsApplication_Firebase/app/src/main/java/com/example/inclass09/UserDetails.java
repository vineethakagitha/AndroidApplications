package com.example.inclass09;

import java.util.ArrayList;

public class UserDetails {

    User u;
    ArrayList<Contact> contacts;


    public UserDetails(User u)
    {
        this.u = u;
        contacts = new ArrayList<>();
    }

    public void addContact(Contact c)
    {
        contacts.add(c);
    }

    public ArrayList<Contact> getContacts()
    {
        return contacts;
    }

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
