package org.example.zboruri.repos;

import org.example.zboruri.models.Flight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightRepository implements IRepository<Integer, Flight> {

    private final String url;
    private final String username;
    private final String password;

    public FlightRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Flight> find(Integer id) {
        String sql = "SELECT * FROM flights WHERE id = ?";
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
    public Iterable<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                flights.add(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    @Override
    public Optional<Flight> save(Flight entity) {
        String sql = "INSERT INTO flights (from_location, to_location, departure_time, landing_time, seats) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getFrom());
            statement.setString(2, entity.getTo());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDepartureTime()));
            statement.setTimestamp(4, Timestamp.valueOf(entity.getLandingTime()));
            statement.setInt(5, entity.getSeats());
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
    public Optional<Flight> update(Flight entity) {
        String sql = "UPDATE flights SET from_location = ?, to_location = ?, departure_time = ?, landing_time = ?, seats = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getFrom());
            statement.setString(2, entity.getTo());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDepartureTime()));
            statement.setTimestamp(4, Timestamp.valueOf(entity.getLandingTime()));
            statement.setInt(5, entity.getSeats());
            statement.setInt(6, entity.getId());
            if (statement.executeUpdate() > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Flight extractEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String fromLocation = resultSet.getString("from_location");
        String toLocation = resultSet.getString("to_location");
        Timestamp departureTime = resultSet.getTimestamp("departure_time");
        Timestamp landingTime = resultSet.getTimestamp("landing_time");
        int seats = resultSet.getInt("seats");
        Flight flight= new Flight( fromLocation, toLocation, departureTime.toLocalDateTime(), landingTime.toLocalDateTime(), seats);
        flight.setId(id);
        return flight;
    }
}
