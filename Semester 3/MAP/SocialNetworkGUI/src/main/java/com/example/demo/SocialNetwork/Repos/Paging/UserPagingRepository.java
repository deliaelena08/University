package com.example.demo.SocialNetwork.Repos.Paging;

import com.example.demo.SocialNetwork.Model.Pages.Page;
import com.example.demo.SocialNetwork.Model.Pages.Pageable;
import com.example.demo.SocialNetwork.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserPagingRepository implements PagingRepository<Long, User> {
    private final List<User> users = new ArrayList<>();

    @Override
    public Page<User> findPage(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int start = pageNumber * pageSize;
        int end = Math.min(start + pageSize, users.size());

        List<User> pageContent = users.subList(start, end);
        return new Page<>(pageContent, users.size());
    }

    @Override
    public Optional<User> save(User entity) {
        users.add(entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<User> delete(Long id) {
        Optional<User> user= find(id);
        if(user.isPresent()) {
            users.remove(user.get());
            return user;
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        return users;
    }

    @Override
    public Optional<User> find(Long id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst();
    }
}
