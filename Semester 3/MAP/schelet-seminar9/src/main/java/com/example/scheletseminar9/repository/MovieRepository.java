package com.example.scheletseminar9.repository;

import com.example.scheletseminar9.domain.Movie;
import com.example.scheletseminar9.dto.MovieFilterDTO;
import com.example.scheletseminar9.util.Paging.Page;
import com.example.scheletseminar9.util.Paging.Pageable;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends PagingRepository<Long, Movie>{
    Optional<Movie> update(Movie entity);

    List<Integer> getYears();
    Page<Movie> findAllOnPage(Pageable pageable, MovieFilterDTO movieFilterDTO);
}
