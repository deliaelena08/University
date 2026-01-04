package com.example.demo.SocialNetwork.Services;

import com.example.demo.SocialNetwork.Model.Message;
import com.example.demo.SocialNetwork.Model.User;
import com.example.demo.SocialNetwork.Repos.Memo.RepositoryMessage;
import com.example.demo.SocialNetwork.Repos.DB.RepositoryUserDB;
import com.example.demo.SocialNetwork.Utils.Events.EventType;
import com.example.demo.SocialNetwork.Utils.Events.SendMessageEvent;
import com.example.demo.SocialNetwork.Utils.Observer.Observable;
import com.example.demo.SocialNetwork.Utils.Observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatService implements Observable<SendMessageEvent> {
    public static ChatService instance;
    RepositoryMessage userMessageRepository;
    RepositoryUserDB userDB;
    private List<Observer<SendMessageEvent>> observers=new ArrayList<>();

    public ChatService(RepositoryMessage userMessageRepository, RepositoryUserDB userDB) {
        this.userMessageRepository = userMessageRepository;
        this.userDB = userDB;
    }

    public static ChatService getInstance(RepositoryMessage userMessageRepository, RepositoryUserDB userDB) {
        if (instance == null) {
            instance = new ChatService(userMessageRepository, userDB);
        }
        return instance;
    }

    public void addMessage(Long sender,Long receiver,String message){
        Optional<User> user1 = userDB.find(sender);
        Optional<User> user2 = userDB.find(receiver);
        if (user1.isPresent() && user2.isPresent()) {
            Message message1=new Message(message,user1.get(),user2.get(), LocalDateTime.now());
            userMessageRepository.save(message1);
            notifyObservers(new SendMessageEvent(EventType.SENDMESSAGE,message1));
        }
    }

    public void deleteMessage(Long id_message){
        userMessageRepository.delete(id_message);
    }

    public Iterable<Message> getChat(Long sender, Long receiver) {
        return userMessageRepository.findChat(sender,receiver);
    }
    @Override
    public void addObserver(Observer<SendMessageEvent> event) {
        observers.add(event);
        System.out.println("Observer added: " + event);
    }

    @Override
    public void removeObserver(Observer<SendMessageEvent> event) {
        observers.remove(event);
    }

    @Override
    public void notifyObservers(SendMessageEvent t) {
        observers.forEach(observer -> observer.update(t));
    }
}
