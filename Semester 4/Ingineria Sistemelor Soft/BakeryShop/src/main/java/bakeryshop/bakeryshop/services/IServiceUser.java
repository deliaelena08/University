package bakeryshop.bakeryshop.services;

import bakeryshop.bakeryshop.models.User;

public interface IServiceUser extends IService<User> {
    User login(String username, String password);
}
