package com.example.demo.SocialNetwork.Utils.Observer;

import com.example.demo.SocialNetwork.Utils.Events.Event;

public interface Observable<E extends Event> {
    public void addObserver(Observer<E> event);
    public void removeObserver(Observer<E> event);
    public void notifyObservers(E t);
}
