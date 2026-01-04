package Repos;

import Helper.Entity;
import Helper.Tuple;
import Model.Friendship;
import Model.User;
import Validations.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RepositoryFriendshipDB implements Repository<Tuple<User, User>, Friendship> {
    private final Validator<Friendship> validator;
    private final String url;
    private final String username;
    private final String password;
    private final Repository<Long,User> repo;

    public RepositoryFriendshipDB(Validator<Friendship> friendshipValidator,String url, String username, String password, Repository<Long,User> repo) {
        this.validator = friendshipValidator;
        this.url = url;
        this.username = username;
        this.password = password;
        this.repo = repo;
    }

    private Friendship createFriendship(ResultSet rs) throws SQLException {
        Long user_id1 = rs.getLong("user_id1");
        Long user_id2 = rs.getLong("user_id2");
        LocalDateTime date = rs.getTimestamp("date_friendship").toLocalDateTime();
        Optional<User> user1=repo.find(user_id1);
        Optional<User> user2=repo.find(user_id2);
        Tuple<User,User> tuple=new Tuple<>(user1.get(),user2.get());
        Friendship friendship= new Friendship();
        friendship.setId(tuple);
        friendship.setDate(date);
        return friendship;
    }

    @Override
    public Optional<Friendship> find(Tuple<User, User> o) {
        String sql = "SELECT * FROM friendships WHERE user_id1 = ? AND user_id2 = ?";
        try(Connection connection= DriverManager.getConnection(url,username,password); PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1, o.getE1().getId());
            statement.setLong(2, o.getE2().getId());

            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                Friendship friendship=createFriendship(resultSet);
                return Optional.of(friendship);
            }
        }catch (SQLException e) {
            System.out.println("Nu s-a gasit prietenia bazei de date");
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendships=new ArrayList<>();
        String sql = "SELECT * FROM friendships";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet resultSet=statement.executeQuery()){
            while(resultSet.next()){
                Friendship friendship=createFriendship(resultSet);
                friendships.add(friendship);
            }
        }catch (SQLException e) {
            System.out.println("Nu s-a gasit prietenii bazei de date");
        }
        return friendships;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        validator.validate(entity);
        String sql="INSERT INTO friendships VALUES (?,?,?)";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1, entity.getId().getE1().getId());
            statement.setLong(2, entity.getId().getE2().getId());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
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
    public Optional<Friendship> delete(Tuple<User, User> o) {
        Optional<Friendship> friendship=find(o);
        if(friendship.isPresent()){
            String sql="DELETE FROM friendships WHERE user_id1 = ? AND user_id2 = ?";
            try(Connection connection=DriverManager.getConnection(url,username,password);
                PreparedStatement statement=connection.prepareStatement(sql)){
                statement.setLong(1, o.getE1().getId());
                statement.setLong(2, o.getE2().getId());
                int rows=statement.executeUpdate();
                if(rows>0){
                    return friendship;
                }
            } catch (SQLException e){
                System.out.println("Nu s-a putut sterge: "+e.getMessage());
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        validator.validate(entity);
        String sql="UPDATE friendships SET date_friendship = ? WHERE user_id1 = ? AND user_id2 = ?";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setTimestamp(1, Timestamp.valueOf(entity.getDate()));
            statement.setLong(2, entity.getId().getE1().getId());
            statement.setLong(3, entity.getId().getE2().getId());
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
