package org.example.ofertedevacanta.repos;

import org.example.ofertedevacanta.models.Hotel;
import org.example.ofertedevacanta.models.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelRepository implements IRepository<Double, Hotel> {

    private final String url;
    private final String username;
    private final String password;

    public HotelRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Hotel> find(Double id) {
        String sql = "SELECT * FROM hotels WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Hotel(
                        resultSet.getDouble("location_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("no_rooms"),
                        resultSet.getDouble("price_per_night"),
                        Type.valueOf(resultSet.getString("type"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Hotel> findAll() {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Hotel hotel = new Hotel(
                        resultSet.getDouble("location_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("no_rooms"),
                        resultSet.getDouble("price_per_night"),
                        Type.valueOf(resultSet.getString("type"))
                );
                hotel.setId(resultSet.getDouble("id"));
                hotels.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }

    @Override
    public Optional<Hotel> save(Hotel entity) {
        String sql = "INSERT INTO hotels (id, location_id, name, no_rooms, price_per_night, type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, entity.getId());
            statement.setDouble(2, entity.getLocationId());
            statement.setString(3, entity.getName());
            statement.setInt(4, entity.getNoRooms());
            statement.setDouble(5, entity.getPricePerNight());
            statement.setString(6, entity.getType().name());
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Hotel> update(Hotel entity) {
        String sql = "UPDATE hotels SET location_id = ?, name = ?, no_rooms = ?, price_per_night = ?, type = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, entity.getLocationId());
            statement.setString(2, entity.getName());
            statement.setInt(3, entity.getNoRooms());
            statement.setDouble(4, entity.getPricePerNight());
            statement.setString(5, entity.getType().name());
            statement.setDouble(6, entity.getId());
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

