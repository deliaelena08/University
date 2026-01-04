package org.example.restaurant.Validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}