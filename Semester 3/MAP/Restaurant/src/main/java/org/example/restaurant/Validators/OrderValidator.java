package org.example.restaurant.Validators;

import org.example.restaurant.models.Order;

public class OrderValidator implements Validator<Order> {
    @Override
    public void validate(Order entity) throws ValidationException {
        if (entity.getTableId() <= 0) {
            throw new ValidationException("The table id must be positive");
        }
        if (entity.getDate().equals("")) {
            throw new ValidationException("The product date must be configured");
        }
        if (entity.getStatus() == null) {
            throw new ValidationException("The status must be not null");
        }
    }
}
