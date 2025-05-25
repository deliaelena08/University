package org.example.anar.validations;

import org.example.anar.models.River;

public class RiverValidator implements Validator<River> {
    @Override
    public void validate(River river) throws ValidationException {
        if (river == null) {
            throw new ValidationException("River is null");
        }
        if (river.getName() == null || river.getName().isEmpty()) {
            throw new ValidationException("River name is null or empty");
        }
        if (river.getLength() <= 0) {
            throw new ValidationException("River length is less than or equal to 0");
        }
    }
}
