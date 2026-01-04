package org.example.taximetrie.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}