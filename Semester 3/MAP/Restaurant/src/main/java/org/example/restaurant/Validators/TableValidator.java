package org.example.restaurant.Validators;

import org.example.restaurant.models.Table;

public class TableValidator implements Validator<Table> {
    @Override
    public void validate(Table entity) throws ValidationException {
        if (entity.getSeats() <= 0) {
            throw new ValidationException("The number of seats must be positive");
        }
    }
}
