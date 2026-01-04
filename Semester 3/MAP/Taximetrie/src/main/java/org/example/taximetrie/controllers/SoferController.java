package org.example.taximetrie.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.example.taximetrie.models.Client;
import org.example.taximetrie.models.Comanda;
import org.example.taximetrie.models.Sofer;
import org.example.taximetrie.services.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class SoferController {

    private Service service;
    private Sofer sofer;
    @FXML
    private ListView<String> ordersListView; // Adaugă în FXML

    @FXML
    private ListView<String> clientsList;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label averageOrdersLabel;

    @FXML
    private Label mostLoyalClientLabel;

    public void setServiceAndSofer(Service service, Sofer sofer) {
        this.service = service;
        this.sofer = sofer;
        loadClients();
        calculateStatistics();
    }

    private void loadClients() {
        List<Client> clients = service.findClientsForSofer(sofer.getId());
        System.out.println("Clienți pentru șoferul " + sofer.getName() + ": " + clients);

        clientsList.getItems().clear();
        clients.forEach(client -> clientsList.getItems().add(client.getName()));
    }

    @FXML
    private void acceptOrder() {
        // Extragere comandă activă pentru șoferul curent
        List<Comanda> comenziActive = service.getComenzi().stream()
                .filter(comanda -> comanda.getSofer().getId().equals(sofer.getId()))
                .collect(Collectors.toList());

        if (comenziActive.isEmpty()) {
            System.out.println("Nu există comenzi active pentru acest șofer.");
            return;
        }

        Comanda comanda = comenziActive.get(0); // Alegem prima comandă activă
        System.out.println("Comanda acceptată de șofer: " + comanda);

        // Actualizează comanda în baza de date
        service.update(comanda);
    }


    @FXML
    private void rejectOrder() {
        // Extragere comandă activă pentru șoferul curent
        List<Comanda> comenziActive = service.getComenzi().stream()
                .filter(comanda -> comanda.getSofer().getId().equals(sofer.getId()))
                .collect(Collectors.toList());

        if (comenziActive.isEmpty()) {
            System.out.println("Nu există comenzi active pentru acest șofer.");
            return;
        }

        Comanda comanda = comenziActive.get(0); // Alegem prima comandă activă
        System.out.println("Comanda respinsă de șofer: " + comanda);

        // Șterge comanda din baza de date
        service.deleteComanda(comanda.getId());
    }

    @FXML
    private void viewOrdersForDate() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            System.out.println("Selectați o dată.");
            return;
        }

        List<Comanda> orders = service.findComenziByDate(sofer.getId(), selectedDate.atStartOfDay());
        if (orders.isEmpty()) {
            System.out.println("Nu există comenzi pentru data selectată.");
        } else {
            orders.forEach(order -> System.out.println("Comandă: " + order));
        }
    }



    private void calculateStatistics() {
        double average = service.findAverageComenziPerDay(sofer.getId());
        averageOrdersLabel.setText("Media comenzilor: " + average);

        Client mostLoyalClient = service.findMostLoyalClient(sofer.getId());
        if (mostLoyalClient != null) {
            mostLoyalClientLabel.setText("Cel mai fidel client: " + mostLoyalClient.getName());
        }
    }
}
