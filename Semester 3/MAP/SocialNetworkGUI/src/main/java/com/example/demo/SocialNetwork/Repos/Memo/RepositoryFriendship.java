package com.example.demo.SocialNetwork.Repos.Memo;

import com.example.demo.SocialNetwork.Helper.Tuple;
import com.example.demo.SocialNetwork.Model.Friends.Friendship;
import com.example.demo.SocialNetwork.Model.User;
import com.example.demo.SocialNetwork.Repos.DB.AbstractFileRepository;
import com.example.demo.SocialNetwork.Repos.Repository;
import com.example.demo.SocialNetwork.Validations.Validator;

import java.time.LocalDateTime;
import java.util.Optional;

public class RepositoryFriendship extends AbstractFileRepository<Tuple<User, User>, Friendship> {
    private final Repository<Long,User> repo;
    public RepositoryFriendship(Validator<Friendship> validator,String fileName, Repository<Long,User> repo) {
        super(validator, fileName);
        this.repo = repo;
        //am nevoie de repo inainte de super in cazul in care loadData l-as fi pastrat in abstract
        // file repo asa ca am pus separat in fiecare constructor pentru a ma asigura de existenta repo-ului
        loadData();
    }

    @Override
    protected Friendship createEntity(String line) {
        String[] fields = line.split("\\|");
        Optional<User> u1=repo.find(Long.parseLong(fields[0]));
        Optional<User> u2=repo.find(Long.parseLong(fields[1]));
        LocalDateTime date = LocalDateTime.parse(fields[2]);
        Friendship friendship=new Friendship();
        friendship.setDate(date);
        Tuple<User,User> tuplu=new Tuple<>(u1.get(),u2.get());
        friendship.setId(tuplu);
        return friendship;
    }

    @Override
    protected String saveEntity(Friendship entity) {
        Tuple<User,User> tuple=entity.getId();
        User u1=tuple.getE1();
        User u2=tuple.getE2();
        String line=u1.getId()+"|"+u2.getId()+"|"+entity.getDate();
        return line;
    }
}
