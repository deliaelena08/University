package org.example.taximetrie.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import org.example.taximetrie.models.Client;
import org.example.taximetrie.models.Persoana;
import org.example.taximetrie.models.Sofer;
import org.example.taximetrie.services.Service;

import java.io.IOException;

public class LoginController {

    private Service service;

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    public void initialize() {
        // Adaugă efect de hover pentru buton
        loginButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event ->
                loginButton.setStyle("-fx-background-color: #005fa8; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 16; -fx-border-radius: 5;")
        );
        loginButton.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
                loginButton.setStyle("-fx-background-color: #0078d4; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 16; -fx-border-radius: 5;")
        );
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        Persoana persoana = service.getPersoane().stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (persoana == null) {
            System.out.println("Invalid username");
            return;
        }

        if (persoana instanceof Client) {
            openClientView((Client) persoana);
        } else if (persoana instanceof Sofer) {
            openSoferView((Sofer) persoana);
        } else {
            System.out.println("Unknown user type");
        }
    }

    private void openClientView(Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/taximetrie/views/client-view.fxml"));
            Scene scene = new Scene(loader.load());

            ClientController clientController = loader.getController();
            clientController.setServiceAndClient(service, client);

            Stage stage = new Stage();
            stage.setTitle("Client - " + client.getName());
            stage.setScene(scene);
            stage.show();

            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSoferView(Sofer sofer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/taximetrie/views/sofer-view.fxml"));
            Scene scene = new Scene(loader.load());

            SoferController soferController = loader.getController();
            soferController.setServiceAndSofer(service, sofer);

            Stage stage = new Stage();
            stage.setTitle("Taximetrist - " + sofer.getName());
            stage.setScene(scene);
            stage.show();

            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }
}
