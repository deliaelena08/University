package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.User;
import bakeryshop.bakeryshop.models.UserType;

public interface IRepoUser extends IRepository<User>{
    User login(String email, String password);
}
