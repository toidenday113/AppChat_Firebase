package com.example.appchat.Model;

public class MessegerGroupChat {

    private String id;
    private String sender;
    private String content;
    private String image;

    public MessegerGroupChat() {
    }

    public MessegerGroupChat(String id, String sender, String content, String image) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

