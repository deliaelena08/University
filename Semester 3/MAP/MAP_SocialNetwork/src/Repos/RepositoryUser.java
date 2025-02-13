package Repos;

import Model.User;
import Validations.Validator;

public class RepositoryUser extends AbstractFileRepository<Long, User> {

    public RepositoryUser(Validator<User> validator, String fileName) {
        super(validator, fileName);
        loadData();
    }

    @Override
    protected User createEntity(String line) {
        String[] fields = line.split("\\|");
        User u =new User(fields[1],fields[2],fields[3],fields[4]);
        u.setId(Long.parseLong(fields[0]));
        return u;
    }

    @Override
    protected String saveEntity(User entity) {
        String s=entity.getId()+"|"+entity.getFirstName()+"|"+entity.getLastName()+"|"+entity.getEmail()+"|"+entity.getPassword();
        return s;
    }
}
