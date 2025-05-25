package com.example.demo.SocialNetwork.Model.Friends;

import com.example.demo.SocialNetwork.Helper.Entity;
import com.example.demo.SocialNetwork.Helper.Tuple;
import com.example.demo.SocialNetwork.Model.User;

import java.time.LocalDateTime;

public class FriendRequest extends Entity<Tuple<User,User>> {
    private String status;
    private LocalDateTime time;

    public User getSender() {
        return getId().getE1();
    }

    public String getSenderFirstName() {
        return getSender().getFirstName();
    }

    public String getSenderLastName() {
        return getSender().getLastName();
    }

    public User getReceiver() {
        return getId().getE2();
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
