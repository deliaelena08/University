package org.example.ofertedevacanta.repos;
import org.example.ofertedevacanta.models.Client;
import org.example.ofertedevacanta.models.Hobby;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements IRepository<Double, Client> {

    private final String url;
    private final String username;
    private final String password;

    public ClientRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Client> find(Double id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Client(
                        resultSet.getString("name"),
                        resultSet.getInt("fidelity_grade"),
                        resultSet.getInt("age"),
                        Hobby.valueOf(resultSet.getString("hobby"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getString("name"),
                        resultSet.getInt("fidelity_grade"),
                        resultSet.getInt("age"),
                        Hobby.valueOf(resultSet.getString("hobby"))
                );
                client.setId(resultSet.getDouble("id"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Optional<Client> save(Client entity) {
        String sql = "INSERT INTO clients (id, name, fidelity_grade, age, hobby) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setInt(3, entity.getFidelityGrade());
            statement.setInt(4, entity.getAge());
            statement.setString(5, entity.getHobby().name());
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Client entity) {
        String sql = "UPDATE clients SET name = ?, fidelity_grade = ?, age = ?, hobby = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getFidelityGrade());
            statement.setInt(3, entity.getAge());
            statement.setString(4, entity.getHobby().name());
            statement.setDouble(5, entity.getId());
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

