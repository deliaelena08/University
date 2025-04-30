package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.User;

import javax.persistence.TypedQuery;

public class RepoDBUser extends RepoDb<User> implements IRepoUser {
    public RepoDBUser() {
        super(User.class);
    }

    @Override
    public User login(String username, String password) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username AND u.password = :password",
                    User.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);

            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
