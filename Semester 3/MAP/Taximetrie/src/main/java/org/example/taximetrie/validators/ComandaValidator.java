package org.example.taximetrie.validators;

import org.example.taximetrie.models.Comanda;

public class ComandaValidator implements Validator<Comanda> {
    @Override
    public void validate(Comanda entity) throws ValidationException {
        if (entity.getClient() == null || entity.getClient().getName().equals("")) {
            throw new ValidationException("Nume client invalid!");
        }
        if (entity.getSofer() == null || entity.getSofer().getName().equals("")) {
            throw new ValidationException("Adresa invalida!");
        }
        if (entity.getData().equals("")) {
            throw new ValidationException("Data invalid!");
        }
    }
}
