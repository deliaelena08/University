package org.example.taximetrie.validators;

import org.example.taximetrie.models.Client;

public class ClientValidator implements Validator<Client> {
    @Override
    public void validate(Client entity) throws ValidationException {
        String errors = "";
        if (entity.getUsername().equals("")) {
            errors += "Username invalid!\n";
        }
        if (entity.getName().equals("")) {
            errors += "Nume invalid!\n";
        }
        if (!errors.equals("")) {
            throw new ValidationException(errors);
        }
    }
}
