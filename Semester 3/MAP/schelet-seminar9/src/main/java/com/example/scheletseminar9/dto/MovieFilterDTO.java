package com.example.scheletseminar9.dto;

import com.example.scheletseminar9.domain.Entity;

import java.util.Optional;

public class MovieFilterDTO extends Entity<Long> {

    private Optional<Integer> year = Optional.empty();
    private Optional<Integer> yearAfter = Optional.empty();
    private Optional<String> title = Optional.empty();
    private Optional<String> director = Optional.empty();

    public Optional<Integer> getYear() {
        return year;
    }

    public Optional<Integer> getYearAfter() {
        return yearAfter;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getDirector() {
        return director;
    }

    public void setYear(Optional<Integer> year) {
        this.year = year;
    }

    public void setYearAfter(Optional<Integer> yearAfter) {
        this.yearAfter = yearAfter;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public void setDirector(Optional<String> director) {
        this.director = director;
    }
}
