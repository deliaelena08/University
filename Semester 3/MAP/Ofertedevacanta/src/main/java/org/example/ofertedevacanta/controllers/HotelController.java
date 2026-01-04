package org.example.ofertedevacanta.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.ofertedevacanta.models.Hotel;
import org.example.ofertedevacanta.models.Location;
import org.example.ofertedevacanta.models.Reservation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class HotelController {

    private MainController mainController;

    @FXML
    private ComboBox<Location> locationComboBox;

    @FXML
    private TableView<Hotel> hotelsTable;

    @FXML
    private TableColumn<Hotel, String> nameColumn;

    @FXML
    private TableColumn<Hotel, Integer> noRoomsColumn;

    @FXML
    private TableColumn<Hotel, Double> pricePerNightColumn;

    @FXML
    private TableColumn<Hotel, String> typeColumn;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        initializeView();
    }

    private void initializeView() {
        List<Location> locations = mainController.getService().getLocations();
        locationComboBox.setItems(FXCollections.observableArrayList(locations));

        locationComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Location location, boolean empty) {
                super.updateItem(location, empty);
                if (empty || location == null) {
                    setText(null);
                } else {
                    setText(location.getName());
                }
            }
        });

        locationComboBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Location location, boolean empty) {
                super.updateItem(location, empty);
                if (empty || location == null) {
                    setText(null);
                } else {
                    setText(location.getName());
                }
            }
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        noRoomsColumn.setCellValueFactory(new PropertyValueFactory<>("noRooms"));
        pricePerNightColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        locationComboBox.setOnAction(event -> loadHotelsForLocation());
        hotelsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Dublu-clic
                Hotel selectedHotel = hotelsTable.getSelectionModel().getSelectedItem();
                if (selectedHotel != null) {
                    openReservationView(selectedHotel);
                }
            }
        });

    }

    private void loadHotelsForLocation() {
        Location selectedLocation = locationComboBox.getValue();
        if (selectedLocation != null) {
            List<Hotel> hotels = mainController.getService().getHotelsByLocation(selectedLocation.getId());
            hotelsTable.setItems(FXCollections.observableArrayList(hotels));
        }
    }

    private void openReservationView(Hotel selectedHotel) {
        if (mainController.getCurrentClient() == null) {
            System.out.println("No client is currently logged in.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ofertedevacanta/views/reservation-view.fxml"));
            Scene scene = new Scene(loader.load());

            ReservationController reservationController = loader.getController();
            reservationController.setMainControllerAndHotel(mainController, selectedHotel);

            Stage stage = new Stage();
            stage.setTitle("Make a Reservation at " + selectedHotel.getName());
            stage.setScene(scene);
            stage.setWidth(600);
            stage.setHeight(400);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
