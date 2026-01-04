package com.example.demo.SocialNetwork.Model;

import com.example.demo.SocialNetwork.Helper.Entity;

public class UserProfile extends Entity <Long>{
    String description;
    String photoUrl;
    public UserProfile(String description, String photoUrl) {
        this.description = description;
        this.photoUrl = photoUrl;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
