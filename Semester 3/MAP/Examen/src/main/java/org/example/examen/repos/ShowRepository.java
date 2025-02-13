package org.example.examen.repos;

import org.example.examen.models.Show;
import org.example.examen.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowRepository implements IRepository<Integer, Show> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Show> validator;

    public ShowRepository(String url, String username, String password, Validator<Show> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<Show> find(Integer id) {
        String query = "SELECT * FROM shows WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToShow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Show> findAll() {
        List<Show> shows = new ArrayList<>();
        String query = "SELECT * FROM shows";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                shows.add(mapResultSetToShow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shows;
    }

    @Override
    public Optional<Show> save(Show show) {
        validator.validate(show);
        String query = "INSERT INTO shows (name, duration_minutes, start_date, number_of_seats, creation_date) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, show.getName());
            statement.setInt(2, show.getDurationMinutes());
            statement.setTimestamp(3, Timestamp.valueOf(show.getStartDate()));
            statement.setInt(4, show.getNumberOfSeats());
            statement.setTimestamp(5, Timestamp.valueOf(show.getCreationDate()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                show.setId(resultSet.getInt("id"));
                return Optional.of(show);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Show> update(Show show) {
        validator.validate(show);

        String query = "UPDATE shows SET name = ?, duration_minutes = ?, start_date = ?, number_of_seats = ?, creation_date = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, show.getName());
            statement.setInt(2, show.getDurationMinutes());
            statement.setTimestamp(3, Timestamp.valueOf(show.getStartDate()));
            statement.setInt(4, show.getNumberOfSeats());
            statement.setTimestamp(5, Timestamp.valueOf(show.getCreationDate()));
            statement.setInt(6, show.getId());
            statement.executeUpdate();
            return Optional.of(show);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Show mapResultSetToShow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int durationMinutes = resultSet.getInt("duration_minutes");
        LocalDateTime startDate = resultSet.getTimestamp("start_date").toLocalDateTime();
        int numberOfSeats = resultSet.getInt("number_of_seats");
        LocalDateTime creationDate = resultSet.getTimestamp("creation_date").toLocalDateTime();
        Show show = new Show(name, durationMinutes, startDate, numberOfSeats, creationDate);
        show.setId(id);
        return show;
    }
    @Override
    public List<Show> findPaginatedAndSorted(int pageIndex, int pageSize) {
        String query = "SELECT * FROM shows ORDER BY start_date ASC LIMIT ? OFFSET ?";
        List<Show> shows = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, pageSize * pageIndex);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                shows.add(mapResultSetToShow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shows;
    }

    public String getOverlappingShowName(LocalDateTime startDate, int durationMinutes) {
        String query = """
        SELECT name FROM shows
        WHERE (? < (start_date + INTERVAL '1 minute' * duration_minutes) AND ? >= start_date)
           OR (start_date < (? + INTERVAL '1 minute' * ?) AND start_date >= ?)
        LIMIT 1;
    """;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            Timestamp startTimestamp = Timestamp.valueOf(startDate);
            Timestamp endTimestamp = Timestamp.valueOf(startDate.plusMinutes(durationMinutes));

            statement.setTimestamp(1, startTimestamp);
            statement.setTimestamp(2, startTimestamp);
            statement.setTimestamp(3, startTimestamp);
            statement.setInt(4, durationMinutes);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name"); // Returnează numele spectacolului
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Dacă nu se găsește niciun spectacol suprapus
    }


}
