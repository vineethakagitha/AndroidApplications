package com.example.inclass10;

import java.io.Serializable;

public class Note implements Serializable {

    String id;
    String userid;
    String text;

    @Override
    public String toString() {
        return "Note{" + "id='" + id + '\'' + ", userid='" + userid + '\'' + ", text='" + text + '\'' + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
