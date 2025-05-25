package org.example.anar.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.anar.models.River;
import org.example.anar.services.Service;

public class RiversController {

    private Service service;

    @FXML
    private TableView<River> riversTable;
    @FXML
    private TableColumn<River, String> nameColumn;
    @FXML
    private TableColumn<River, Integer> lengthColumn;
    @FXML
    private TextField riverIdField;
    @FXML
    private TextField newLevelField;
    @FXML
    private Button updateButton;

    public void setService(Service service) {
        this.service = service;
        loadRivers();
    }

    private void loadRivers() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        riversTable.getItems().setAll(service.getRivers());
    }
    @FXML
    private void refreshRivers() {
        loadRivers();
    }
    @FXML
    private void updateRiverLevel() {
        try {
            int riverId = Integer.parseInt(riverIdField.getText());
            int newLevel = Integer.parseInt(newLevelField.getText());
            service.updateRiverLevel(riverId, newLevel);
            loadRivers(); // Reîncarcă tabelul râurilor
        } catch (Exception e) {
            System.err.println("Error updating river level: " + e.getMessage());
        }
    }
}

