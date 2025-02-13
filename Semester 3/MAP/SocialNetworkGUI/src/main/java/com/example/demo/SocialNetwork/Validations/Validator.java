package com.example.demo.SocialNetwork.Validations;

import com.example.demo.SocialNetwork.Exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}