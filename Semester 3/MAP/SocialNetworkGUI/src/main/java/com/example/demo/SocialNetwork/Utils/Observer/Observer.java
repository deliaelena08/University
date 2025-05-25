package com.example.demo.SocialNetwork.Utils.Observer;


import com.example.demo.SocialNetwork.Utils.Events.Event;

public interface Observer<E extends Event> {
    void update(E event);
}
