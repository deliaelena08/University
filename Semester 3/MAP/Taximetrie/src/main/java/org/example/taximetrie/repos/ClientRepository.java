package org.example.taximetrie.repos;

import org.example.taximetrie.models.Client;
import org.example.taximetrie.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements IRepository<Long, Client> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Client> validator;

    public ClientRepository(String url, String username, String password, Validator<Client> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<Client> find(Long id) {
        String query = "SELECT * FROM persoana WHERE id = ? AND type = 'CLIENT'";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToClient(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM persoana WHERE type = 'CLIENT'";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                clients.add(mapResultSetToClient(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Optional<Client> save(Client client) {
        validator.validate(client);
        String query = "INSERT INTO persoana (username, name, type) VALUES (?, ?, 'CLIENT')";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, client.getUsername());
            statement.setString(2, client.getName());
            statement.executeUpdate();
            return Optional.of(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Client client) {
        validator.validate(client);
        String query = "UPDATE persoana SET username = ?, name = ? WHERE id = ? AND type = 'CLIENT'";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getUsername());
            statement.setString(2, client.getName());
            statement.setLong(3, client.getId());
            statement.executeUpdate();
            return Optional.of(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Client mapResultSetToClient(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String name = resultSet.getString("name");
        Client client=new Client(username, name);
        client.setId(id);
        return client;
    }
}
