package com.example.appchat.Model;

public class mGroup {
    private String id;
    private String idGroup;

    public mGroup(String id, String idGroup) {
        this.id = id;
        this.idGroup = idGroup;
    }

    public mGroup() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    @Override
    public String toString() {
        return "mGroup{" +
                "id='" + id + '\'' +
                ", idGroup='" + idGroup + '\'' +
                '}';
    }
}
