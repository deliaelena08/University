package org.example.adoptii.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}