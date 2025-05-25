package org.example.taximetrie.repos;

import org.example.taximetrie.models.Client;
import org.example.taximetrie.models.Comanda;
import org.example.taximetrie.models.Persoana;
import org.example.taximetrie.models.Sofer;
import org.example.taximetrie.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComandaRepository implements IRepository<Long, Comanda> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Comanda> validator;
    private final PersoanaRepository persoanaRepository;
    private final SoferRepository soferRepository;
    public ComandaRepository(String url, String username, String password, Validator<Comanda> validator, PersoanaRepository persoanaRepository, SoferRepository soferRepository) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.persoanaRepository = persoanaRepository;
        this.soferRepository = soferRepository;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<Comanda> find(Long id) {
        String query = "SELECT * FROM comanda WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToComanda(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Comanda> findAll() {
        List<Comanda> comenzi = new ArrayList<>();
        String query = "SELECT * FROM comanda";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                try {
                    Comanda comanda = mapResultSetToComanda(resultSet);
                    if (comanda != null) {
                        comenzi.add(comanda);
                    }
                } catch (Exception e) {
                    System.err.println("Eroare la maparea comenzii: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comenzi;
    }



    @Override
    public Optional<Comanda> save(Comanda comanda) {
        validator.validate(comanda);
        String query = "INSERT INTO comanda (client_id, sofer_id, data) VALUES (?, ?, ?) RETURNING id";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, comanda.getClient().getId());
            statement.setLong(2, comanda.getSofer().getId());
            statement.setTimestamp(3, Timestamp.valueOf(comanda.getData()));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                comanda.setId(resultSet.getLong("id")); // Setează ID-ul generat pe obiectul Comanda
            }

            return Optional.of(comanda);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public Optional<Comanda> update(Comanda comanda) {
        validator.validate(comanda);
        String query = "UPDATE comanda SET client_id = ?, sofer_id = ?, data = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, comanda.getClient().getId());
            statement.setLong(2, comanda.getSofer().getId());
            statement.setTimestamp(3, Timestamp.valueOf(comanda.getData()));
            statement.setLong(4, comanda.getId());
            statement.executeUpdate();
            return Optional.of(comanda);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Comanda mapResultSetToComanda(ResultSet resultSet) throws SQLException {
        Long comandaId = resultSet.getLong("id");
        Long clientId = resultSet.getLong("client_id");
        Long soferId = resultSet.getLong("sofer_id");
        LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();

        Persoana client = persoanaRepository.find(clientId).orElse(null);
        Sofer sofer = soferRepository.find(soferId).orElse(null);

        if (client == null) {
            System.err.println("Clientul cu ID-ul " + clientId + " nu a fost găsit în baza de date.");
        }
        if (sofer == null) {
            System.err.println("Șoferul cu ID-ul " + soferId + " nu a fost găsit în baza de date.");
        }

        if (client != null && sofer != null) {
            Comanda comanda = new Comanda((Client) client, sofer, data);
            comanda.setId(comandaId);
            return comanda;
        }

        throw new IllegalStateException("Comanda cu ID-ul " + comandaId + " nu poate fi mapată deoarece clientul sau șoferul nu există.");
    }



}
