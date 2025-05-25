package com.example.scheletseminar9.repository;

import com.example.scheletseminar9.domain.Entity;
import com.example.scheletseminar9.domain.Movie;
import com.example.scheletseminar9.util.Paging.Page;
import com.example.scheletseminar9.util.Paging.Pageable;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);

}
