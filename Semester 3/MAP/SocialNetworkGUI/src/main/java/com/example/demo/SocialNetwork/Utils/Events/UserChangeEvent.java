package com.example.demo.SocialNetwork.Utils.Events;

import com.example.demo.SocialNetwork.Model.User;

public class UserChangeEvent implements Event {
    private EventType eventType;
    private User newuser;
    private User oldUser;

    public UserChangeEvent(EventType eventType, User newuser) {
        this.eventType = eventType;
        this.newuser = newuser;
    }

    public UserChangeEvent(EventType eventType, User newuser, User oldUser) {
        this.eventType = eventType;
        this.newuser = newuser;
        this.oldUser = oldUser;
    }

    public EventType getEventType() {
        return eventType;
    }

    public User getNewuser() {
        return newuser;
    }

    public User getOlduser() {
        return oldUser;
    }
}
