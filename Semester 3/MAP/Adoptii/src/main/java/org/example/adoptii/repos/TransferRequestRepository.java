package org.example.adoptii.repos;
import org.example.adoptii.models.TransferRequests;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransferRequestRepository implements IRepository<Integer, TransferRequests> {
    private final String url;
    private final String username;
    private final String password;

    public TransferRequestRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<TransferRequests> find(Integer id) {
        String sql = "SELECT * FROM transfer_requests WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
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
    public Iterable<TransferRequests> findAll() {
        List<TransferRequests> requests = new ArrayList<>();
        String sql = "SELECT * FROM transfer_requests";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                requests.add(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public Optional<TransferRequests> save(TransferRequests transferRequests) {
        String sql = "INSERT INTO transfer_requests (animal_id, from_center_id, to_center_id, status) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transferRequests.getAnimalId());
            statement.setInt(2, transferRequests.getFromCenterId());
            statement.setInt(3, transferRequests.getToCenterId());
            statement.setString(4, transferRequests.getStatus());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                transferRequests.setId(resultSet.getInt("id")); // Setează ID-ul generat
                return Optional.of(transferRequests);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<TransferRequests> findByAnimalId(Integer animalId) {
        List<TransferRequests> requests = new ArrayList<>();
        String sql = "SELECT * FROM transfer_requests WHERE animal_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, animalId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    requests.add(extractEntity(resultSet)); // Metoda care creează obiectul TransferRequests
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }


    @Override
    public Optional<TransferRequests> update(TransferRequests entity) {
        String sql = "UPDATE transfer_requests SET status = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getStatus());
            statement.setInt(2, entity.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }



    private TransferRequests extractEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int animalId = resultSet.getInt("animal_id");
        int fromCenterId = resultSet.getInt("from_center_id");
        int toCenterId = resultSet.getInt("to_center_id");
        String status = resultSet.getString("status");
        TransferRequests transferRequests = new TransferRequests(animalId, fromCenterId, toCenterId, status);
        transferRequests.setId(id);
        return transferRequests;
    }
}
