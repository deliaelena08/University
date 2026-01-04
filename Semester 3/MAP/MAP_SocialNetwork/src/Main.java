import Model.Friendship;
import Model.User;
import Repos.RepositoryFriendshipDB;
import Repos.RepositoryUserDB;
import Services.SocialNetworkService;
import UserInterfaces.ConsoleUI;
import Validations.FriendshipValidator;
import Validations.UserValidator;
import Validations.Validator;

public class Main {
    public static void main(String[] args) {
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        RepositoryUserDB userDB=new RepositoryUserDB(userValidator,"jdbc:postgresql://localhost:5432/User","postgres","Delia.08");
        RepositoryFriendshipDB friendshipDB=new RepositoryFriendshipDB(friendshipValidator,"jdbc:postgresql://localhost:5432/User","postgres","Delia.08",userDB);
        SocialNetworkService service=new SocialNetworkService(userDB,friendshipDB);
        ConsoleUI ui=new ConsoleUI(service);
        ui.start();
    }
}