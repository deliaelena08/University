package com.example.demo.SocialNetwork.Validations;

import com.example.demo.SocialNetwork.Exceptions.ValidationException;
import com.example.demo.SocialNetwork.Model.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        //TODO: implement method validate
        if(entity.getFirstName().equals(""))
            throw new ValidationException("Utilizatorul nu este valid");
        if(entity.getLastName().equals(""))
            throw new ValidationException("Utilizatorul nu este valid");
        if(entity.getEmail().equals(""))
            throw new ValidationException("Emailul nu este valid");
        if(entity.getPassword().equals("") || entity.getPassword().length() < 6)
            throw new ValidationException("Parola nu este valida");
    }
}
