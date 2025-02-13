package com.example.demo.SocialNetwork.Repos.Paging;

import com.example.demo.SocialNetwork.Helper.Entity;
import com.example.demo.SocialNetwork.Model.Pages.Page;
import com.example.demo.SocialNetwork.Model.Pages.Pageable;
import com.example.demo.SocialNetwork.Repos.Repository;

public interface PagingRepository<ID, T extends Entity<ID>> extends Repository<ID, T> {
    Page<T> findPage(Pageable pageable);
}
