package com.example.demo.SocialNetwork.Model;

import com.example.demo.SocialNetwork.Helper.Entity;

import java.time.LocalDateTime;

public class Message extends Entity<Long> {
    private String message;
    private User sender;
    private User receiver;
    private LocalDateTime time;
    public Message(String message, User sender, User receiver, LocalDateTime time) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public User getSender() {
        return sender;
    }
    public User getReceiver() {
        return receiver;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
