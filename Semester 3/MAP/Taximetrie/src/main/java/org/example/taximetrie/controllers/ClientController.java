package org.example.taximetrie.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.taximetrie.models.Client;
import org.example.taximetrie.models.Comanda;
import org.example.taximetrie.models.Sofer;
import org.example.taximetrie.services.Service;

import java.time.LocalDateTime;
import java.util.List;

public class ClientController {

    private Service service;
    private Client client;

    @FXML
    private TextField locationField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> activeOrdersList;

    @FXML
    private ListView<String> driversList;

    @FXML
    private void handleSearch() {
        String locatie = locationField.getText();
        if (locatie == null || locatie.trim().isEmpty()) {
            System.out.println("Locația nu poate fi goală.");
            return;
        }

        // Găsește șoferii pe baza locației
        List<Sofer> soferi = service.findSoferiByLocation(locatie);
        driversList.getItems().clear();

        if (soferi.isEmpty()) {
            driversList.getItems().add("Nu s-au găsit șoferi pentru locația specificată.");
        } else {
            soferi.forEach(sofer -> {
                // Creează comanda și notifică utilizatorul
                service.addComanda(client, sofer, LocalDateTime.now());
                driversList.getItems().add("Sofer: " + sofer.getName() + ", Indicativ: " + sofer.getIndicativMasina());
            });
        }
    }


    public void setServiceAndClient(Service service, Client client) {
        this.service = service;
        this.client = client;
    }

//    @FXML
//    private void searchForTaxi() {
//        String location = locationField.getText();
//        List<Sofer> soferi = service.findSoferiByLocation(location);
//
//        activeOrdersList.getItems().clear();
//        soferi.forEach(sofer -> activeOrdersList.getItems().add("Sofer: " + sofer.getName() + ", Indicativ: " + sofer.getIndicativMasina()));
//
//        // Simulare creare comandă
//        if (!soferi.isEmpty()) {
//            Sofer selectedSofer = soferi.get(0); // Alegem primul șofer ca exemplu
//            Comanda comanda = service.addComanda(client, selectedSofer, LocalDateTime.now());
//            System.out.println("Comanda creată: " + comanda);
//        }
//    }
}
