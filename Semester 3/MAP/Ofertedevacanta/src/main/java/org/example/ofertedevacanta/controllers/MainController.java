package org.example.ofertedevacanta.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import org.example.ofertedevacanta.models.Client;
import org.example.ofertedevacanta.services.Service;

import java.io.IOException;
import java.util.List;

public class MainController {

    private Service service;
    private Client currentClient;

    @FXML
    private Button hotelsButton;

    @FXML
    private Button clientsButton;
    @FXML
    private ComboBox<Client> clientComboBox;


    public void setService(Service service) {
        this.service = service;

        // Inițializează clienții în ComboBox
        List<Client> clients = service.getClients();
        clientComboBox.setItems(FXCollections.observableArrayList(clients));

        clientComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getName());
                }
            }
        });

        clientComboBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getName());
                }
            }
        });

        clientComboBox.setOnAction(event -> {
            currentClient = clientComboBox.getValue();
            System.out.println("Current client set to: " + (currentClient != null ? currentClient.getName() : "None"));
        });
    }


    public Service getService() {
        return service;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client client) {
        this.currentClient = client;
    }

    @FXML
    private void openHotelsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ofertedevacanta/views/hotel-view.fxml"));
            Scene scene = new Scene(loader.load());
            HotelController hotelController = loader.getController();
            hotelController.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Hotels");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openClientsView() {
        List<Client> clients = service.getClients();
        for (Client client : clients) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ofertedevacanta/views/client-view.fxml"));
                Scene scene = new Scene(loader.load());

                ClientController clientController = loader.getController();
                clientController.setMainControllerAndClient(this, client);

                Stage stage = new Stage();
                stage.setTitle("Offers for " + client.getName());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
