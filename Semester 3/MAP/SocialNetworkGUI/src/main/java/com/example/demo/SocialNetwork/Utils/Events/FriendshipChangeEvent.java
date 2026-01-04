package com.example.demo.SocialNetwork.Utils.Events;

import com.example.demo.SocialNetwork.Model.Friends.Friendship;
import javafx.event.ActionEvent;

public class FriendshipChangeEvent implements Event {
    private EventType eventType;
    private Friendship friendship;
    public FriendshipChangeEvent(EventType eventType, Friendship friendship) {
        this.eventType = eventType;
        this.friendship = friendship;
    }
    public EventType getEventType() {
        return eventType;
    }
    public Friendship getFriendship() {
        return friendship;
    }

    public void handleSave(ActionEvent actionEvent) {
    }

    public void handleCancel(ActionEvent actionEvent) {
        
    }
}
