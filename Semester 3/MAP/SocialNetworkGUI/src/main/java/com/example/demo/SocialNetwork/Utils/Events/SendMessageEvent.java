package com.example.demo.SocialNetwork.Utils.Events;

import com.example.demo.SocialNetwork.Model.Message;

public class SendMessageEvent implements Event {
    private Message message;
    private EventType event;
    public SendMessageEvent( EventType event, Message message) {
        this.message = message;
        this.event = event;
    }
    public Message getMessage(){
        return message;
    }

    public EventType getEvent() {
        return event;
    }
}
