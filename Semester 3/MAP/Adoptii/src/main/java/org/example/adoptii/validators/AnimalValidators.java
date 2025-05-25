package org.example.adoptii.validators;

import org.example.adoptii.models.Animal;

public class AnimalValidators implements Validator<Animal> {
    @Override
    public void validate(Animal entity) throws ValidationException {
        if (entity.getName().equals("")) {
            throw new ValidationException("Name cannot be empty!");
        }
        if (entity.getAdoptionCenterId() < 0) {
            throw new ValidationException("Adoption center id must be a positive integer!");
        }
        if (entity.getType() == null) {
            throw new ValidationException("Type cannot be null!");
        }
    }
}
