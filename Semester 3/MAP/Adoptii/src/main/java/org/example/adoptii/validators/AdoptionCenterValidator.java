package org.example.adoptii.validators;

import org.example.adoptii.models.AdoptionCenter;

public class AdoptionCenterValidator implements Validator<AdoptionCenter> {
    @Override
    public void validate(AdoptionCenter entity) throws ValidationException {
        if (entity.getName().equals("")) {
            throw new ValidationException("Name cannot be empty!");
        }
        if (entity.getLocation().equals("")) {
            throw new ValidationException("Location cannot be empty!");
        }
        if (entity.getCapacity() < 0) {
            throw new ValidationException("Capacity must be a positive integer!");
        }
    }
}
