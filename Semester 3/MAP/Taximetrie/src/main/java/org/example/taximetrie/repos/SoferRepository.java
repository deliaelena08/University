package org.example.taximetrie.repos;

import org.example.taximetrie.models.Sofer;
import org.example.taximetrie.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoferRepository implements IRepository<Long, Sofer> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Sofer> validator;

    public SoferRepository(String url, String username, String password, Validator<Sofer> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<Sofer> find(Long persoanaId) {
        String query = "SELECT s.persoana_id, s.indicativ_masina, p.username, p.name " +
                "FROM sofer s " +
                "JOIN persoana p ON s.persoana_id = p.id " +
                "WHERE s.persoana_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, persoanaId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToSofer(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Sofer> findAll() {
        List<Sofer> soferi = new ArrayList<>();
        String query = "SELECT s.persoana_id, s.indicativ_masina, p.username, p.name " +
                "FROM sofer s " +
                "JOIN persoana p ON s.persoana_id = p.id";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                soferi.add(mapResultSetToSofer(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return soferi;
    }

    @Override
    public Optional<Sofer> save(Sofer sofer) {
        validator.validate(sofer);
        String queryPersoana = "INSERT INTO persoana (username, name, type) VALUES (?, ?, 'SOFER') RETURNING id";
        String querySofer = "INSERT INTO sofer (persoana_id, indicativ_masina) VALUES (?, ?)";
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            // Salvează în tabelul `persoana`
            try (PreparedStatement statementPersoana = connection.prepareStatement(queryPersoana)) {
                statementPersoana.setString(1, sofer.getUsername());
                statementPersoana.setString(2, sofer.getName());
                ResultSet resultSet = statementPersoana.executeQuery();
                if (resultSet.next()) {
                    Long persoanaId = resultSet.getLong("id");

                    // Salvează în tabelul `sofer`
                    try (PreparedStatement statementSofer = connection.prepareStatement(querySofer)) {
                        statementSofer.setLong(1, persoanaId);
                        statementSofer.setString(2, sofer.getIndicativMasina());
                        statementSofer.executeUpdate();

                        sofer.setId(persoanaId); // Setează ID-ul pentru entitatea `Sofer`
                    }
                }
            }

            connection.commit();
            return Optional.of(sofer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Sofer> update(Sofer sofer) {
        validator.validate(sofer);
        String querySofer = "UPDATE sofer SET indicativ_masina = ? WHERE persoana_id = ?";
        String queryPersoana = "UPDATE persoana SET username = ?, name = ? WHERE id = ?";
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            // Actualizează datele în tabelul `persoana`
            try (PreparedStatement statementPersoana = connection.prepareStatement(queryPersoana)) {
                statementPersoana.setString(1, sofer.getUsername());
                statementPersoana.setString(2, sofer.getName());
                statementPersoana.setLong(3, sofer.getId());
                statementPersoana.executeUpdate();
            }

            // Actualizează datele în tabelul `sofer`
            try (PreparedStatement statementSofer = connection.prepareStatement(querySofer)) {
                statementSofer.setString(1, sofer.getIndicativMasina());
                statementSofer.setLong(2, sofer.getId());
                statementSofer.executeUpdate();
            }

            connection.commit();
            return Optional.of(sofer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Sofer mapResultSetToSofer(ResultSet resultSet) throws SQLException {
        Long persoanaId = resultSet.getLong("persoana_id");
        String username = resultSet.getString("username");
        String name = resultSet.getString("name");
        String indicativMasina = resultSet.getString("indicativ_masina");

        Sofer sofer = new Sofer(username, name, indicativMasina,persoanaId);
        sofer.setId(persoanaId);
        return sofer;
    }
}
