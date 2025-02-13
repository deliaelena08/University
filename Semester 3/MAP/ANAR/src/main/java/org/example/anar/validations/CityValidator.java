package org.example.anar.validations;

import org.example.anar.models.City;

public class CityValidator implements Validator<City> {
    @Override
    public void validate(City city) throws ValidationException {
        if (city == null) {
            throw new ValidationException("City is null");
        }
        if (city.getName() == null || city.getName().isEmpty()) {
            throw new ValidationException("City name is null or empty");
        }
        if (city.getCmdr() <= 0) {
            throw new ValidationException("City population is less than or equal to 0");
        }
    }
}
