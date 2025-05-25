package org.example.ofertedevacanta.repos;

import org.example.ofertedevacanta.models.Reservation;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RservationRepository implements IRepository<Double, Reservation> {
    private final String url;
    private final String username;
    private final String password;

    public RservationRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Reservation> find(Double id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                reservations.add(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public Optional<Reservation> save(Reservation entity) {
        String sql = "INSERT INTO reservations (client_id, hotel_id, start_date, no_nights) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, entity.getClientId());
            statement.setDouble(2, entity.getHotelId());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getStartDate()));
            statement.setInt(4, entity.getNoNights());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getDouble(1));
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Reservation> update(Reservation entity) {
        String sql = "UPDATE reservations SET client_id = ?, hotel_id = ?, start_date = ?, no_nights = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, entity.getClientId());
            statement.setDouble(2, entity.getHotelId());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getStartDate()));
            statement.setInt(4, entity.getNoNights());
            statement.setDouble(5, entity.getId());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Reservation extractEntity(ResultSet resultSet) throws SQLException {
        double id = resultSet.getDouble("id");
        double clientId = resultSet.getDouble("client_id");
        double hotelId = resultSet.getDouble("hotel_id");
        LocalDateTime startDate = resultSet.getTimestamp("start_date").toLocalDateTime();
        int noNights = resultSet.getInt("no_nights");
        Reservation reservation = new Reservation(clientId, hotelId, startDate, noNights);
        reservation.setId(id);
        return reservation;
    }
}
