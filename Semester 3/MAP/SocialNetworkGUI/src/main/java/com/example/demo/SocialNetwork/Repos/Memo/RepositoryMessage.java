package com.example.demo.SocialNetwork.Repos.Memo;

import com.example.demo.SocialNetwork.Model.Message;
import com.example.demo.SocialNetwork.Repos.Repository;

public interface RepositoryMessage extends Repository<Long, Message> {
    Iterable<Message> findChat(Long sender, Long receiver);
}
