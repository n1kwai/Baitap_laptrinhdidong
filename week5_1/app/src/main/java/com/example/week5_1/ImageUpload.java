package com.example.week5_1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUpload {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("avatar")
    @Expose
    private String avatar;
    public ImageUpload() {
    }
    public ImageUpload(int id, String username, String avatar) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}