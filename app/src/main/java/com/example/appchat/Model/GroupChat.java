package com.example.appchat.Model;

public class GroupChat {
    private String id;
    private String name;
    private String avatar;
    private String admin;

    public GroupChat() {
    }

    public GroupChat(String id, String name, String avatar, String admin) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "GroupChat{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", admin='" + admin + '\'' +
                '}';
    }
}
