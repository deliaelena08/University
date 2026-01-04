package bakeryshop.bakeryshop.services;

import bakeryshop.bakeryshop.models.User;
import bakeryshop.bakeryshop.repos.IRepoUser;

public class ServiceUser extends ServiceDB<User> implements IServiceUser {

    private final IRepoUser userRepo;
    public ServiceUser(IRepoUser userRepo) {
        super(userRepo);
        this.userRepo = userRepo;
    }

    @Override
    public User login(String username, String password) {
        return userRepo.login(username, password);
    }
}
