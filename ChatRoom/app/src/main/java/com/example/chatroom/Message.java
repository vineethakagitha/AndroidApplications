package com.example.chatroom;

public class Message {

    String text;
    String image;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Message{" + "text='" + text + '\'' + ", image='" + image + '\'' + '}';
    }

}

