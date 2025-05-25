package org.example.examen.validators;

import org.example.examen.models.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email-ul nu poate fi gol.");
        }
        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email-ul nu este valid.");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("Numele utilizatorului nu poate fi gol.");
        }
    }
}
