package org.example.ofertedevacanta.controllers;

import com.sun.tools.javac.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.ofertedevacanta.models.Hotel;
import org.example.ofertedevacanta.models.SpecialOffer;
import org.example.ofertedevacanta.services.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class OffersController {

    private MainController mainController;
    private Hotel selectedHotel;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableView<SpecialOffer> offersTable;

    @FXML
    private TableColumn<SpecialOffer, Date> startDateColumn;

    @FXML
    private TableColumn<SpecialOffer, Date> endDateColumn;

    @FXML
    private TableColumn<SpecialOffer, Integer> percentColumn;

    public void setServiceAndHotel(MainController mainController, Hotel hotel) {
        this.mainController = mainController;
        this.selectedHotel = hotel;
        initializeView();
    }

    private void initializeView() {
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        percentColumn.setCellValueFactory(new PropertyValueFactory<>("percents"));

        startDatePicker.setOnAction(event -> loadOffers());
        endDatePicker.setOnAction(event -> loadOffers());
    }

    private void loadOffers() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        if (start != null && end != null) {
            List<SpecialOffer> offers = mainController.getService().getOffersByHotelandPeriod(
                    selectedHotel.getId(),
                    java.sql.Date.valueOf(start),
                    java.sql.Date.valueOf(end)
            );
            offersTable.setItems(FXCollections.observableArrayList(offers));
        }
        offersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double click
                SpecialOffer selectedOffer = offersTable.getSelectionModel().getSelectedItem();
                if (selectedOffer != null) {
                    Hotel hotel = mainController.getService().getHotel(selectedOffer.getHotelId());
                    openReservationView(hotel, selectedOffer);
                }
            }
        });
    }

    private void openReservationView(Hotel hotel, SpecialOffer offer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ofertedevacanta/views/reservation-view.fxml"));
            Scene scene = new Scene(loader.load());

            ReservationController reservationController = loader.getController();
            reservationController.setMainControllerAndHotel(mainController, hotel);

            Stage stage = new Stage();
            stage.setTitle("Make a Reservation at " + hotel.getName());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
