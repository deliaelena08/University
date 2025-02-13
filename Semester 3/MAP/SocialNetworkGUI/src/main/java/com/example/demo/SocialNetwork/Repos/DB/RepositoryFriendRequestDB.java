package com.example.demo.SocialNetwork.Repos.DB;

import com.example.demo.SocialNetwork.Helper.Tuple;
import com.example.demo.SocialNetwork.Model.Friends.FriendRequest;
import com.example.demo.SocialNetwork.Model.User;
import com.example.demo.SocialNetwork.Repos.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositoryFriendRequestDB implements Repository<Tuple<User, User>, FriendRequest> {
    private final String url;
    private final String username;
    private final String password;
    private final Repository<Long,User> repo;

    public RepositoryFriendRequestDB(String url, String username, String password, Repository<Long, User> repo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.repo = repo;
    }

    private FriendRequest createRequest(ResultSet rs) throws SQLException {
        Long user_id1 = rs.getLong("sender");
        Long user_id2 = rs.getLong("receiver");
        String status=rs.getString("status");
        LocalDateTime date = rs.getTimestamp("send_date").toLocalDateTime();
        Optional<User> user1=repo.find(user_id1);
        Optional<User> user2=repo.find(user_id2);
        Tuple<User,User> tuple=new Tuple<>(user1.get(),user2.get());
        FriendRequest request= new FriendRequest();
        request.setId(tuple);
        request.setStatus(status);
        request.setTime(date);
        return request;
    }

    @Override
    public Optional<FriendRequest> find(Tuple<User, User> userUserTuple) {
        String sql = "SELECT * FROM friendrequests WHERE sender = ? AND receiver = ?";
        try(Connection connection= DriverManager.getConnection(url,username,password); PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1, userUserTuple.getE1().getId());
            statement.setLong(2, userUserTuple.getE2().getId());

            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                FriendRequest request=createRequest(resultSet);
                return Optional.of(request);
            }
        }catch (SQLException e) {
            System.out.println("Nu s-a gasit cererea de prietenie in baza de date");
        }
        return Optional.empty();
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        List<FriendRequest> requests=new ArrayList<>();
        String sql = "SELECT * FROM friendrequests";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet resultSet=statement.executeQuery()){
            while(resultSet.next()){
                FriendRequest request=createRequest(resultSet);
                requests.add(request);
            }
        }catch (SQLException e) {
            System.out.println("Nu s-a gasit cereri in baza de date");
        }
        return requests;
    }

    @Override
    public Optional<FriendRequest> save(FriendRequest entity) {
        String sql="INSERT INTO friendrequests VALUES (?,?,?,?)";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1, entity.getId().getE1().getId());
            statement.setLong(2, entity.getId().getE2().getId());
            statement.setString(3, entity.getStatus());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getTime()));
            int rows=statement.executeUpdate();
            if(rows>0){
                return Optional.of(entity);
            }
        }catch (SQLException e) {
            System.out.println("Nu s-a putut salva: "+e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<FriendRequest> delete(Tuple<User, User> userUserTuple) {
        Optional<FriendRequest> request=find(userUserTuple);
        if(request.isPresent()){
            String sql="DELETE FROM friendrequests WHERE sender = ? AND receiver = ?";
            try(Connection connection=DriverManager.getConnection(url,username,password);
                PreparedStatement statement=connection.prepareStatement(sql)){
                statement.setLong(1, userUserTuple.getE1().getId());
                statement.setLong(2, userUserTuple.getE2().getId());
                int rows=statement.executeUpdate();
                if(rows>0){
                    return request;
                }
            } catch (SQLException e){
                System.out.println("Nu s-a putut sterge: "+e.getMessage());
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<FriendRequest> update(FriendRequest entity) {
        String sql="UPDATE friendrequests SET status = ?, send_date = ? WHERE sender = ? AND receiver = ?";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1, entity.getStatus());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getTime()));
            statement.setLong(3, entity.getId().getE1().getId());
            statement.setLong(4, entity.getId().getE2().getId());
            int rows=statement.executeUpdate();
            if(rows>0){
                return Optional.of(entity);
            }
        }catch(SQLException e){
            System.out.println("Nu s-a putut face update: "+e.getMessage());
        }
        return Optional.empty();
    }
}
