package org.example.taximetrie.validators;

import org.example.taximetrie.models.Persoana;

public class PersoanaValidator implements Validator<Persoana> {
    @Override
    public void validate(Persoana entity) throws ValidationException {
        if (entity.getUsername() == null || entity.getUsername().equals("")) {
            throw new ValidationException("Username invalid!");
        }
        if (entity.getName() == null || entity.getName().equals("")) {
            throw new ValidationException("Name invalid!");
        }
    }
}
