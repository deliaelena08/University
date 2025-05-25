package org.example.zboruri.repos;

import org.example.zboruri.models.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketRepository implements IRepository<Integer, Ticket> {

    private final String url;
    private final String username;
    private final String password;

    public TicketRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Ticket> find(Integer id) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                tickets.add(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public Optional<Ticket> save(Ticket entity) {
        String sql = "INSERT INTO tickets (username, flight_id, purchase_time) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getUsername());
            statement.setInt(2, entity.getFlightId());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getPurchaseTime()));
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                    return Optional.of(entity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> update(Ticket entity) {
        String sql = "UPDATE tickets SET username = ?, flight_id = ?, purchase_time = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getUsername());
            statement.setInt(2, entity.getFlightId());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getPurchaseTime()));
            statement.setInt(4, entity.getId());
            if (statement.executeUpdate() > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Ticket extractEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        int flightId = resultSet.getInt("flight_id");
        Timestamp purchaseTime = resultSet.getTimestamp("purchase_time");
        Ticket ticket= new Ticket( username, flightId, purchaseTime.toLocalDateTime());
        ticket.setId(id);
        return ticket;
    }
}
