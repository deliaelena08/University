package org.example.examen.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.example.examen.models.Show;
import org.example.examen.services.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddShowController {

    private Service service;

    @FXML
    private TextField nameField;

    @FXML
    private TextField durationField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField seatsField;

    @FXML
    private Button saveButton;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    private void handleSaveShow() {
        try {
            String name = nameField.getText();
            int duration = Integer.parseInt(durationField.getText());
            LocalDateTime startDate = LocalDateTime.of(startDatePicker.getValue(), LocalTime.parse(startTimeField.getText()));
            int seats = Integer.parseInt(seatsField.getText());

            // Verificare spectacol suprapus
            String overlappingShowName = service.getOverlappingShowName(startDate, duration);
            if (overlappingShowName != null) {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "The show cannot be scheduled at the specified time as " + overlappingShowName + " will be playing at that time.");
                return;
            }

            service.addShow(name, duration, startDate, seats, LocalDateTime.now());
            showAlert(Alert.AlertType.INFORMATION, "Success", "The show has been successfully added!");

            UserViewController.notifyAllUsers();

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input! Please check all fields.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
