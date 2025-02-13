package org.example.taximetrie.repos;

import org.example.taximetrie.models.Client;
import org.example.taximetrie.models.Persoana;
import org.example.taximetrie.models.Sofer;
import org.example.taximetrie.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersoanaRepository implements IRepository<Long, Persoana> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Persoana> validator;

    public PersoanaRepository(String url, String username, String password, Validator<Persoana> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<Persoana> find(Long id) {
        String query = "SELECT * FROM persoana WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToPersoana(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Persoana> findAll() {
        List<Persoana> persoane = new ArrayList<>();
        String query = "SELECT * FROM persoana";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Persoana persoana = mapResultSetToPersoana(resultSet);
                persoane.add(persoana);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persoane;
    }

    private Persoana mapResultSetToPersoana(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String name = resultSet.getString("name");
        String type = resultSet.getString("type");

        if ("CLIENT".equalsIgnoreCase(type)) {
             Client client=new Client(username, name);
             client.setId(id);
            return client;
        } else if ("SOFER".equalsIgnoreCase(type)) {
            Sofer sofer= new Sofer(username, name, null,id);
            sofer.setId(id);
            return sofer;
        }
        Persoana persoana= new Persoana(username, name);
        persoana.setId(id);
        return persoana;
    }


    @Override
    public Optional<Persoana> save(Persoana persoana) {
        validator.validate(persoana);
        String query = "INSERT INTO persoana (id, username, name) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, persoana.getId());
            statement.setString(2, persoana.getUsername());
            statement.setString(3, persoana.getName());
            statement.executeUpdate();
            return Optional.of(persoana);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Persoana> update(Persoana persoana) {
        validator.validate(persoana);
        String query = "UPDATE persoana SET username = ?, name = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, persoana.getUsername());
            statement.setString(2, persoana.getName());
            statement.setLong(3, persoana.getId());
            statement.executeUpdate();
            return Optional.of(persoana);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

//    private Persoana mapResultSetToPersoana(ResultSet resultSet) throws SQLException {
//        Long id = resultSet.getLong("id");
//        String username = resultSet.getString("username");
//        String name = resultSet.getString("name");
//        return new Persoana(username, name);
//    }
}
