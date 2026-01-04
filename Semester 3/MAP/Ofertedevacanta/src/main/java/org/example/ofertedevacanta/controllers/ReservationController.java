package org.example.ofertedevacanta.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import org.example.ofertedevacanta.models.Hotel;
import org.example.ofertedevacanta.models.Reservation;

import java.time.LocalDateTime;

public class ReservationController {

    private MainController mainController;
    private Hotel selectedHotel;

    @FXML
    private Label hotelNameLabel;

    @FXML
    private Label clientNameLabel;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private Spinner<Integer> noNightsSpinner;

    public void setMainControllerAndHotel(MainController mainController, Hotel hotel) {
        this.mainController = mainController;
        this.selectedHotel = hotel;
        initializeView();
    }

    private void initializeView() {
        hotelNameLabel.setText("Hotel: " + selectedHotel.getName());
        if (mainController.getCurrentClient() == null) {
            clientNameLabel.setText("Client: Not logged in");
        } else {
            clientNameLabel.setText("Client: " + mainController.getCurrentClient().getName());
        }

        noNightsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 1));
    }


    @FXML
    private void handleMakeReservation() {
        LocalDateTime startDate = startDatePicker.getValue().atStartOfDay();
        int noNights = noNightsSpinner.getValue();

        if (startDate == null || noNights <= 0) {
            showAlert("Invalid Data", "Please select a valid start date and number of nights.");
            return;
        }

        mainController.getService().addReservation(
                mainController.getCurrentClient().getId(),
                selectedHotel.getId(),
                startDate,
                noNights
        );

        Reservation reservation = new Reservation(
                mainController.getCurrentClient().getId(),
                selectedHotel.getId(),
                startDate,
                noNights
        );
        mainController.getService().notifyClientsAboutReservation(reservation);

        showAlert("Reservation Successful", "Reservation made successfully at " + selectedHotel.getName() + "!");
        closeWindow();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) hotelNameLabel.getScene().getWindow();
        stage.close();
    }
}
