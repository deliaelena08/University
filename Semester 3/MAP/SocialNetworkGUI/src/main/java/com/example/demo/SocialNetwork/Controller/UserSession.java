package com.example.demo.SocialNetwork.Controller;
import com.example.demo.SocialNetwork.Model.User;

public class UserSession {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    public static User getCurrentUser() {
        return currentUser;
    }
}