package com.example.appchat.Model;

public class ChatList {
    public String id;

    public ChatList(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChatList{" +
                "id='" + id + '\'' +
                '}';
    }

    public ChatList() {
    }
}
