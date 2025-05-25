package com.example.demo.SocialNetwork.Repos.DB;
import com.example.demo.SocialNetwork.Model.User;
import com.example.demo.SocialNetwork.Model.UserProfile;
import com.example.demo.SocialNetwork.Repos.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositoryProfile implements Repository<Long, UserProfile> {
    private final String url;
    private final String username;
    private final String password;
    public RepositoryProfile(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    private UserProfile createUserProfile(ResultSet rs) throws SQLException {
        String description = rs.getString("description");
        String photo = rs.getString("photo");
        UserProfile userProfile = new UserProfile(description, photo);
        userProfile.setId(rs.getLong("id_user"));
        return userProfile;
    }
    
    @Override
    public Optional<UserProfile> find(Long id) {
        String sql = "SELECT * FROM users_profile WHERE id_user = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UserProfile userProfile = createUserProfile(resultSet);
                return Optional.of(userProfile);
            }
        } catch (SQLException e) {
            System.out.println("Nu s-a putut găsi profilul utilizatorului: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<UserProfile> findAll() {
        List<UserProfile> profiles = new ArrayList<>();
        String sql = "SELECT * FROM users_profile";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UserProfile userProfile = createUserProfile(resultSet);
                profiles.add(userProfile);
            }
        } catch (SQLException e) {
            System.out.println("Nu s-au putut găsi profilurile: " + e.getMessage());
        }
        return profiles;
    }

    @Override
    public Optional<UserProfile> save(UserProfile entity) {
        String sql = "INSERT INTO users_profile (id_user, description, photo) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getDescription());
            statement.setString(3, entity.getPhotoUrl());

            int rows = statement.executeUpdate();
            if (rows > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            System.out.println("Nu s-a putut salva profilul: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserProfile> delete(Long id) {
        Optional<UserProfile> userProfile = find(id);
        if (userProfile.isPresent()) {
            String sql = "DELETE FROM users_profile WHERE id_user = ?";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setLong(1, id);
                int rows = statement.executeUpdate();
                if (rows > 0) {
                    return userProfile;
                }
            } catch (SQLException e) {
                System.out.println("Nu s-a putut șterge profilul: " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserProfile> update(UserProfile entity) {
        String sql = "UPDATE users_profile SET description = ?, photo = ? WHERE id_user = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getDescription());
            statement.setString(2, entity.getPhotoUrl());
            statement.setLong(3, entity.getId());

            int rows = statement.executeUpdate();
            if (rows > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            System.out.println("Nu s-a putut actualiza profilul: " + e.getMessage());
        }
        return Optional.empty();
    }
}
