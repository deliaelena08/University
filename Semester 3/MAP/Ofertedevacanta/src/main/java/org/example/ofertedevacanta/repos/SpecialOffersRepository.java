package org.example.ofertedevacanta.repos;
import org.example.ofertedevacanta.models.SpecialOffer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecialOffersRepository implements IRepository<Double, SpecialOffer> {

    private final String url;
    private final String username;
    private final String password;

    public SpecialOffersRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<SpecialOffer> find(Double id) {
        String sql = "SELECT * FROM special_offers WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new SpecialOffer(
                        resultSet.getDouble("hotel_id"),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getInt("percents")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<SpecialOffer> findAll() {
        List<SpecialOffer> specialOffers = new ArrayList<>();
        String sql = "SELECT * FROM special_offers";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                SpecialOffer specialOffer = new SpecialOffer(
                        resultSet.getDouble("hotel_id"),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getInt("percents")
                );
                specialOffer.setId(resultSet.getDouble("id"));
                specialOffers.add(specialOffer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specialOffers;
    }

    @Override
    public Optional<SpecialOffer> save(SpecialOffer entity) {
        String sql = "INSERT INTO special_offers (id, hotel_id, start_date, end_date, percents) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, entity.getId());
            statement.setDouble(2, entity.getHotelId());
            statement.setDate(3, new java.sql.Date(entity.getStartDate().getTime()));
            statement.setDate(4, new java.sql.Date(entity.getEndDate().getTime()));
            statement.setInt(5, entity.getPercents());
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<SpecialOffer> update(SpecialOffer entity) {
        String sql = "UPDATE special_offers SET hotel_id = ?, start_date = ?, end_date = ?, percents = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, entity.getHotelId());
            statement.setDate(2, new java.sql.Date(entity.getStartDate().getTime()));
            statement.setDate(3, new java.sql.Date(entity.getEndDate().getTime()));
            statement.setInt(4, entity.getPercents());
            statement.setDouble(5, entity.getId());
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
