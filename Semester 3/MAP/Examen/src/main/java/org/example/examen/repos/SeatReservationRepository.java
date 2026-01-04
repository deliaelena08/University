package org.example.examen.repos;

import org.example.examen.models.SeatReservations;
import org.example.examen.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeatReservationRepository implements IRepository<Integer, SeatReservations> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<SeatReservations> validator;

    public SeatReservationRepository(String url, String username, String password, Validator<SeatReservations> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<SeatReservations> find(Integer id) {
        String query = "SELECT * FROM seat_reservations WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToSeatReservations(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<SeatReservations> findAll() {
        List<SeatReservations> reservations = new ArrayList<>();
        String query = "SELECT * FROM seat_reservations";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                reservations.add(mapResultSetToSeatReservations(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public Optional<SeatReservations> save(SeatReservations reservation) {
        validator.validate(reservation);
        String query = "INSERT INTO seat_reservations (user_email, show_id) VALUES (?, ?) RETURNING id";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reservation.getUserEmail());
            statement.setInt(2, reservation.getShowId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                reservation.setId(resultSet.getInt("id"));
                return Optional.of(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<SeatReservations> update(SeatReservations reservation) {
        validator.validate(reservation);
        String query = "UPDATE seat_reservations SET user_email = ?, show_id = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reservation.getUserEmail());
            statement.setInt(2, reservation.getShowId());
            statement.setInt(3, reservation.getId());
            statement.executeUpdate();
            return Optional.of(reservation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private SeatReservations mapResultSetToSeatReservations(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String userEmail = resultSet.getString("user_email");
        int showId = resultSet.getInt("show_id");
        SeatReservations reservation = new SeatReservations(userEmail, showId);
        reservation.setId(id);
        return reservation;
    }
}
