package org.example.zboruri.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.zboruri.models.Client;
import org.example.zboruri.models.Flight;
import org.example.zboruri.service.Service;

import java.time.LocalDate;
import java.util.List;

public class FlightsController {

    private Service service;
    private Client client;

    @FXML
    private Label welcomeLabel;

    @FXML
    private ComboBox<String> fromComboBox;

    @FXML
    private ComboBox<String> toComboBox;

    @FXML
    private DatePicker departureDatePicker;

    @FXML
    private TableView<Flight> flightsTable;

    @FXML
    private TableColumn<Flight, String> fromColumn;

    @FXML
    private TableColumn<Flight, String> toColumn;

    @FXML
    private TableColumn<Flight, String> departureTimeColumn;

    @FXML
    private TableColumn<Flight, String> landingTimeColumn;

    @FXML
    private TableColumn<Flight, Integer> seatsColumn;
    @FXML
    private TableColumn<Flight, Void> actionsColumn;

    @FXML
    private Label pageLabel;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    private int currentPage = 0;
    private final int pageSize = 5;

    private void addActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button buyButton = new Button("Buy Ticket");

            {
                buyButton.setOnAction(event -> {
                    Flight flight = getTableView().getItems().get(getIndex());
                    try {
                        service.purchaseTicket(client.getUsername(), flight.getId());
                        updateFlightsTable(); // Actualizează tabelul după cumpărare
                    } catch (Exception e) {
                        showAlert("Error", e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buyButton);
                }
            }
        });
    }

    private void updateFlightsTable() {
        List<Flight> flights = service.getFlightsPaginated(currentPage, pageSize);
        flightsTable.setItems(FXCollections.observableArrayList(flights));

        int totalPages = service.getTotalPages(pageSize);
        pageLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);

        previousButton.setDisable(currentPage == 0);
        nextButton.setDisable(currentPage >= totalPages - 1);
    }

    private void nextPage() {
        currentPage++;
        updateFlightsTable();
    }

    private void previousPage() {
        currentPage--;
        updateFlightsTable();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setServiceAndClient(Service service, Client client) {
        this.service = service;
        this.client = client;

        initializeView();
    }

    private void initializeView() {
        welcomeLabel.setText("Welcome, " + client.getName() + "!");

        fromComboBox.setItems(FXCollections.observableArrayList(
                service.getAllFlights()
                        .stream()
                        .map(Flight::getFrom)
                        .distinct()
                        .toList()
        ));

        toComboBox.setItems(FXCollections.observableArrayList(
                service.getAllFlights()
                        .stream()
                        .map(Flight::getTo)
                        .distinct()
                        .toList()
        ));

        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        landingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("landingTime"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));

        flightsTable.setItems(FXCollections.observableArrayList(service.getAllFlights()));

        departureDatePicker.setOnAction(event -> filterFlights());
        fromComboBox.setOnAction(event -> filterFlights());
        toComboBox.setOnAction(event -> filterFlights());
        addActionsColumn();

        updateFlightsTable();
        previousButton.setOnAction(event -> previousPage());
        nextButton.setOnAction(event -> nextPage());
    }

    private void filterFlights() {
        String from = fromComboBox.getValue();
        String to = toComboBox.getValue();
        LocalDate date = departureDatePicker.getValue();

        List<Flight> filteredFlights = service.getflightsbydayandcity(
                from,
                to,
                date != null ? date.getDayOfWeek().toString() : ""
        );

        flightsTable.setItems(FXCollections.observableArrayList(filteredFlights));
    }
}
