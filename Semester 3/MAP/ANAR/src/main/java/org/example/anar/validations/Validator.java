package org.example.anar.validations;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}