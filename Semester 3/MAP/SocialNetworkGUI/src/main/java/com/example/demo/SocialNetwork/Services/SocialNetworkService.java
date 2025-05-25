package com.example.demo.SocialNetwork.Services;

import com.example.demo.SocialNetwork.BCrypt.src.org.mindrot.jbcrypt.BCrypt;
import com.example.demo.SocialNetwork.Helper.Tuple;
import com.example.demo.SocialNetwork.Model.*;
import com.example.demo.SocialNetwork.Model.Friends.FriendRequest;
import com.example.demo.SocialNetwork.Model.Friends.Friendship;
import com.example.demo.SocialNetwork.Model.Friends.Network;
import com.example.demo.SocialNetwork.Model.Pages.Page;
import com.example.demo.SocialNetwork.Model.Pages.Pageable;
import com.example.demo.SocialNetwork.Repos.DB.RepositoryProfile;
import com.example.demo.SocialNetwork.Repos.Paging.PagingRepository;
import com.example.demo.SocialNetwork.Repos.Repository;
import com.example.demo.SocialNetwork.Utils.Observer.Observable;
import com.example.demo.SocialNetwork.Utils.Events.EventType;
import com.example.demo.SocialNetwork.Utils.Events.FriendshipChangeEvent;
import com.example.demo.SocialNetwork.Utils.Observer.Observer;

import java.awt.color.ProfileDataException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SocialNetworkService implements Observable<FriendshipChangeEvent> {
    Repository<Tuple<User, User>, Friendship> friendshipRepository;
    Repository<Long, User> userRepository;
    Repository<Tuple<User,User>, FriendRequest> friendrequest;
    Repository<Long,UserProfile> userProfileRepository;
    private PagingRepository<Long, User> pageRepository;
    //private List<Observer<UserChangeEvent>> observersUser=new ArrayList<>();
    private List<Observer<FriendshipChangeEvent>> observersFriendship=new ArrayList<>();

    public SocialNetworkService(Repository<Long, User> userRepository, Repository<Tuple<User, User>, Friendship> friendshipRepository, Repository<Tuple<User,User>,FriendRequest> friendrequest, PagingRepository<Long, User> pageRepository, RepositoryProfile userProfile) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.friendrequest=friendrequest;
        this.pageRepository=pageRepository;
        this.userProfileRepository=userProfile;
    }

    public void addUser(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, email, password);
        userRepository.save(user);
        //UserChangeEvent event=new UserChangeEvent(EventType.ADD,user);
        //notifyObservers(event);
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
       // notifyObservers(new UserChangeEvent(EventType.REMOVE,user));
    }

    public void addFriendship(Long userId1, Long userId2) {
        Optional<User> user1 = userRepository.find(userId1);
        Optional<User> user2 = userRepository.find(userId2);
        if (user1.isPresent() && user2.isPresent()) {
            Friendship friendship = new Friendship();
            friendship.setId(new Tuple<User,User>(user1.get(),user2.get()));
            friendshipRepository.save(friendship);
            notifyObservers(new FriendshipChangeEvent(EventType.ADD,friendship));
        }
    }

    public void deleteFriendship(Long userId1, Long userId2) {
        Optional<User> user1 = userRepository.find(userId1);
        Optional<User> user2 = userRepository.find(userId2);
        if (user1.isPresent() && user2.isPresent()) {
            Tuple<User,User> tuple=new Tuple<>(user1.get(),user2.get());
            Optional<Friendship> friendship= friendshipRepository.delete(tuple);
            notifyObservers(new FriendshipChangeEvent(EventType.REMOVE,friendship.get()));
            Optional<FriendRequest> request = friendrequest.find(tuple);
            request.ifPresent(friendRequest -> friendrequest.delete(friendRequest.getId()));
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

    public List<User> findUsersByName(String name) {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)  // convertește Iterable în Stream
                .filter(user -> user.getFirstName().contains(name) || user.getLastName().contains(name))
                .collect(Collectors.toList());
    }

    public User findUserByEmail(String email){
        return StreamSupport.stream(userRepository.findAll().spliterator(),false)
                .filter(user -> user.getEmail().equals(email))
                .toList().getFirst();
    }

    public List<User> getFriends(Long userId) {
        return StreamSupport.stream(friendshipRepository.findAll().spliterator(), false)  // convertește Iterable în Stream
                .filter(f -> f.getId().getE1().getId().equals(userId) || f.getId().getE2().getId().equals(userId))
                .map(f -> f.getId().getE1().getId().equals(userId) ? f.getId().getE2() : f.getId().getE1())
                .collect(Collectors.toList());
    }

    public int getNumberOfFriends(Long userId) {
        return getFriends(userId).size();
    }


    public User autentification(String email,String password) {
        User user= findUserByEmail(email);
        if (user != null) {
            if (BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public List<FriendRequest> getAllRequests(Long userId) {
        return StreamSupport.stream(friendrequest.findAll().spliterator(), false)
                .filter(req -> req.getReceiver().getId().equals(userId) && req.getStatus().equals("send"))
                .toList();
    }
    public int getNumberOfFriendRequests(Long userId) {
        return getAllRequests(userId).size();
    }


    public void updateRequestStatus(FriendRequest request, String status) {
        request.setStatus(status);
        friendrequest.update(request);
        if(Objects.equals(status, "accepted")){
            addFriendship(request.getId().getE1().getId(),request.getId().getE2().getId());
        }
    }


    public void addRequest(Long sender,Long receiver,String status){
        Optional<User> user1 = userRepository.find(sender);
        Optional<User> user2 = userRepository.find(receiver);
        if (user1.isPresent() && user2.isPresent()) {
            FriendRequest request = new FriendRequest();
            request.setId(new Tuple<User,User>(user1.get(),user2.get()));
            request.setStatus(status);
            request.setTime(LocalDateTime.now());
            friendrequest.save(request);
        }
    }

    public Page<User> getFriendsPaginated(Long userId, Pageable pageable) {
        List<User> friends = getFriends(userId);
        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = Math.min(start + pageable.getPageSize(), friends.size());
        List<User> friendsPage = friends.subList(start, end);
        return new Page<>(friendsPage, friends.size());
    }

    public Map<String, Object> getUserPageData(Long userId) {
        Map<String, Object> userData = new HashMap<>();
        User user = userRepository.find(userId).orElse(null);
        UserProfile profile = userProfileRepository.find(userId).orElse(null);
        int numberOfFriends = getNumberOfFriends(userId);
        int numberOfRequests = getNumberOfFriendRequests(userId);

        if (user != null) {
            userData.put("name", user.getFirstName() + " " + user.getLastName());
            userData.put("email", user.getEmail());
        }

        if (profile != null) {
            userData.put("description", profile.getDescription());
            userData.put("photoUrl", profile.getPhotoUrl());
        }

        userData.put("numberOfFriends", numberOfFriends);
        userData.put("numberOfFriendRequests", numberOfRequests);

        return userData;
    }
    public UserProfile getUserProfile(Long userId) {
        return userProfileRepository.find(userId).orElse(null);
    }
    public void updateUserDescription(Long userId, String description) {
        UserProfile user=new UserProfile(description,getUserProfile(userId).getPhotoUrl());
        user.setId(userId);
        userProfileRepository.update(user);
    }

    public Iterable<FriendRequest> getFriendRequests(){
        return friendrequest.findAll();
    }


    @Override
    public void addObserver(Observer<FriendshipChangeEvent> event) {
        observersFriendship.add(event);
    }

    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> event) {
        observersFriendship.remove(event);
    }

    @Override
    public void notifyObservers(FriendshipChangeEvent t) {
        observersFriendship.forEach(observer -> observer.update(t));
    }
}
