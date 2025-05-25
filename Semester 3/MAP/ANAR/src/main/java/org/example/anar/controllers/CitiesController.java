package org.example.anar.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.example.anar.services.Service;

import java.util.List;
import java.util.Map;

public class CitiesController {

    private Service service;

    @FXML
    private ListView<String> reducedRiskList;
    @FXML
    private ListView<String> mediumRiskList;
    @FXML
    private ListView<String> majorRiskList;
    @FXML
    private VBox reducedRiskBox;
    @FXML
    private VBox mediumRiskBox;
    @FXML
    private VBox majorRiskBox;



    public void setService(Service service) {
        this.service = service;
        loadCityRisks();
    }

    private void loadCityRisks() {
        Map<String, List<String>> cityByRisk = service.getCityByRiver();

        reducedRiskList.getItems().setAll(cityByRisk.getOrDefault("Risc redus", List.of()));
        mediumRiskList.getItems().setAll(cityByRisk.getOrDefault("Risc mediu", List.of()));
        majorRiskList.getItems().setAll(cityByRisk.getOrDefault("Risc major", List.of()));
    }
}
