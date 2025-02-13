package org.example.restaurant.Validators;

import org.example.restaurant.models.MenuItem;

public class MenuItemValidator implements Validator<MenuItem> {
    @Override
    public void validate(MenuItem entity) throws ValidationException {
        if (entity == null) {
            throw new ValidationException("Entity is null");
        }
        if (entity.getItem().equals("")) {
            throw new ValidationException("Name is empty");
        }
        if (entity.getPrice() < 0) {
            throw new ValidationException("Price is negative");
        }
    }
}
