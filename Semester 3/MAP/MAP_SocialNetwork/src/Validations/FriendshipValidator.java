package Validations;

import Exceptions.ValidationException;
import Helper.Tuple;
import Model.Friendship;
import Model.User;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        Tuple<User, User> tuplu=entity.getId();
        User u1=tuplu.getE1();
        User u2=tuplu.getE2();
        if(u1.equals(u2))
            throw new ValidationException("Invalid friendship");
        if(u1.getId()==null || u2.getId()==null)
            throw new ValidationException("Invalid user");
    }
}
