package Services;

import Helper.Tuple;
import Model.Friendship;
import Model.Network;
import Model.User;
import Repos.Repository;
import Repos.RepositoryFriendship;
import Repos.RepositoryUser;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SocialNetworkService {
    Repository<Tuple<User, User>, Friendship> friendshipRepository;
    Repository<Long, User> userRepository;
    public SocialNetworkService(Repository<Long, User> userRepository, Repository<Tuple<User, User>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public void addUser(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, email, password);
        userRepository.save(user);
    }

    private void deleteRelations(User user) {
        for(Friendship f : friendshipRepository.findAll() ) {
            Tuple<User,User> tuple=f.getId();
            if(tuple.getE1().equals(user) || tuple.getE2().equals(user)) {
                friendshipRepository.delete(f.getId());
            }
        }
    }

    public void deleteUser(Long id){
        deleteRelations(userRepository.find(id).get());
        User user=userRepository.delete(id).get();
    }

    public void addFriendship(Long userId1, Long userId2) {
        Optional<User> user1 = userRepository.find(userId1);
        Optional<User> user2 = userRepository.find(userId2);
        if (user1.isPresent() && user2.isPresent()) {
            Friendship friendship = new Friendship();
            friendship.setId(new Tuple<User,User>(user1.get(),user2.get()));
            friendshipRepository.save(friendship);
        }
    }

    public void deleteFriendship(Long userId1, Long userId2) {
        Optional<User> user1 = userRepository.find(userId1);
        Optional<User> user2 = userRepository.find(userId2);
        if (user1.isPresent() && user2.isPresent()) {
            Tuple<User,User> tuple=new Tuple<>(user1.get(),user2.get());
            friendshipRepository.delete(tuple);
        }
    }

    private Network getNetwork(){
        Set<User> users= new HashSet<>();
        Set<Friendship> friendships= new HashSet<>();
        userRepository.findAll().forEach(users::add);
        friendshipRepository.findAll().forEach(friendships::add);
        return new Network(users,friendships);
    }

    public int getComunitiesnumber(){
        Network network=getNetwork();
        return network.numberOfCommunities();
    }

    public Set<User> mostSociableComunity(){
        Network network=getNetwork();
        return network.mostSociableCommunity();
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Iterable<Friendship> getAllFriendships(){
        return friendshipRepository.findAll();
    }
}
