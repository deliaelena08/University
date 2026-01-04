package org.example.ofertedevacanta.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.ofertedevacanta.models.Client;
import org.example.ofertedevacanta.models.Hotel;
import org.example.ofertedevacanta.models.Location;
import org.example.ofertedevacanta.models.SpecialOffer;
import org.example.ofertedevacanta.services.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ClientController {

    private MainController mainController;
    private Client client;

    @FXML
    private Label clientNameLabel;

    @FXML
    private TableView<SpecialOffer> offersTable;

    @FXML
    private TableColumn<SpecialOffer, String> hotelNameColumn;

    @FXML
    private TableColumn<SpecialOffer, String> locationNameColumn;

    @FXML
    private TableColumn<SpecialOffer, Date> startDateColumn;

    @FXML
    private TableColumn<SpecialOffer, Date> endDateColumn;

    public void setMainControllerAndClient(MainController mainController, Client client) {
        this.mainController = mainController;
        this.client = client;
        initializeView();
    }


    private void initializeView() {
        clientNameLabel.setText("Special Offers for: " + client.getName());

        // Configure table columns
        hotelNameColumn.setCellValueFactory(cellData -> {
            Hotel hotel = mainController.getService().getHotel(cellData.getValue().getHotelId());
            return new javafx.beans.property.SimpleStringProperty(hotel != null ? hotel.getName() : "Unknown");
        });

        locationNameColumn.setCellValueFactory(cellData -> {
            Hotel hotel = mainController.getService().getHotel(cellData.getValue().getHotelId());
            Location location = hotel != null ? mainController.getService().getLocation(hotel.getLocationId()) : null;
            return new javafx.beans.property.SimpleStringProperty(location != null ? location.getName() : "Unknown");
        });

        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        // Load offers for the client
        loadOffers();

        // Adăugăm listener pentru clicuri pe rânduri
        offersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Dublu click
                SpecialOffer selectedOffer = offersTable.getSelectionModel().getSelectedItem();
                if (selectedOffer != null) {
                    openReservationView(selectedOffer);
                }
            }
        });
    }

    private void openReservationView(SpecialOffer selectedOffer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ofertedevacanta/views/reservation-view.fxml"));
            Scene scene = new Scene(loader.load());

            ReservationController reservationController = loader.getController();
            reservationController.setMainControllerAndHotel(mainController, mainController.getService().getHotel(selectedOffer.getHotelId()));

            Stage stage = new Stage();
            stage.setTitle("Reservation for Offer");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadOffers() {
        List<SpecialOffer> offers = mainController.getService().getOffers().stream()
                .filter(offer -> isOfferValidForClient(offer))
                .collect(Collectors.toList());
        offersTable.setItems(FXCollections.observableArrayList(offers));
    }

    private boolean isOfferValidForClient(SpecialOffer offer) {
        Hotel hotel = mainController.getService().getHotel(offer.getHotelId());
        return hotel != null
                && client.getFidelityGrade() > offer.getPercents()
                && offer.getEndDate().after(new Date());
    }
}
