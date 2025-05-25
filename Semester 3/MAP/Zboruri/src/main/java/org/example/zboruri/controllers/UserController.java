package org.example.zboruri.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.zboruri.models.Client;
import org.example.zboruri.service.Service;

public class UserController {

    private Service service;

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        Client client = service.getAllClients()
                .stream()
                .filter(c -> c.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (client != null) {
            openFlightsView(client);
        } else {
            showAlert("Login Failed", "No user found with the given username.");
        }
    }

    private void openFlightsView(Client client) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/zboruri/views/flights-view.fxml"));
            Scene scene = new Scene(loader.load());

            FlightsController controller = loader.getController();
            controller.setServiceAndClient(service, client);

            stage.setTitle("Available Flights");
            stage.setScene(scene);
            stage.show();

            ((Stage) usernameField.getScene().getWindow()).close(); // Închide fereastra curentă
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
