package Repos;

import Model.User;
import Validations.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositoryUserDB implements Repository<Long, User> {
    private final Validator<User> validator;
    private final String url;
    private final String username;
    private final String password;

    public RepositoryUserDB(Validator<User> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private User createUser(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        User user = new User(firstName, lastName, email, password);
        user.setId(id);
        return user;
    }
    @Override
    public Optional<User> find(Long aLong) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try(Connection connection= DriverManager.getConnection(url,username,password); PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1, aLong);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                User user=createUser(resultSet);
                return Optional.of(user);
            }
        }catch (SQLException e) {
            System.out.println("Nu s-a gasit userul bazei de date");
        }
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        List<User> users=new ArrayList<>();
        String sql = "SELECT * FROM users";
        try(Connection connection=DriverManager.getConnection(url,username,password);
        PreparedStatement statement=connection.prepareStatement(sql);
        ResultSet resultSet=statement.executeQuery()){
            while(resultSet.next()){
                User user=createUser(resultSet);
                users.add(user);
            }
        }catch (SQLException e) {
            System.out.println("Nu s-a gasit useri bazei de date");
        }
        return users;
    }

    @Override
    public Optional<User> save(User entity) {
        validator.validate(entity);
        String sql="INSERT INTO users(first_name, last_name, email, password) VALUES (?,?,?,?)";
        try(Connection connection=DriverManager.getConnection(url,username,password);
        PreparedStatement statement=connection.prepareStatement(sql)){
            //statement.setLong(1, entity.getId());
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
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
    public Optional<User> delete(Long aLong) {
        Optional<User> user=find(aLong);
        if(user.isPresent()){
            String sql="DELETE FROM users WHERE id = ?";
            try(Connection connection=DriverManager.getConnection(url,username,password);
                PreparedStatement statement=connection.prepareStatement(sql)){
                statement.setLong(1, aLong);
                int rows=statement.executeUpdate();
                if(rows>0){
                    return user;
                }
            } catch (SQLException e){
                System.out.println("Nu s-a putut sterge: "+e.getMessage());
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        validator.validate(entity);
        String sql="UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
        try(Connection connection=DriverManager.getConnection(url,username,password);
        PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            statement.setLong(5, entity.getId());
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
