package org.example.examen.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
