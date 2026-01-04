package org.example.taximetrie.validators;

import org.example.taximetrie.models.Sofer;

public class SoferValidator implements Validator<Sofer> {
    @Override
    public void validate(Sofer entity) throws ValidationException {
        if (entity.getUsername() == null || entity.getUsername().equals("")) {
            throw new ValidationException("Username invalid!");
        }
        if (entity.getName() == null || entity.getName().equals("")) {
            throw new ValidationException("Name invalid!");
        }
        if (entity.getIndicativMasina() == null || entity.getIndicativMasina().equals("")) {
            throw new ValidationException("Indicativ masina invalid!");
        }
    }
}
