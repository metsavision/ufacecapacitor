package com.metsakuur.uface.retrofit;

public class UFaceRequestData {

    private String id;

    private String face;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFace(String face) {
        this.face = face;
    }

    @Override
    public String toString() {
        return "UFaceRequestData{" +
                "id='" + id + '\'' +
                ", face='" + face + '\'' +
                '}';
    }
}
