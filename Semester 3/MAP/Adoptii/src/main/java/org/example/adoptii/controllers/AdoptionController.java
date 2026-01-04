package org.example.adoptii.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.adoptii.models.AdoptionCenter;
import org.example.adoptii.models.Animal;
import org.example.adoptii.models.AnimalType;
import org.example.adoptii.models.TransferRequests;
import org.example.adoptii.services.Service;

import java.util.ArrayList;
import java.util.List;

public class AdoptionController {

    private Service service;
    private AdoptionCenter adoptionCenter;

    @FXML
    private Label centerNameLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label percentageLabel;


    private void updatePercentageLabel() {
        double percentage = service.getProcentageOfAdoptionCenter(adoptionCenter.getId());
        percentageLabel.setText("Occupancy: " + String.format("%.2f", percentage) + "%");
    }


    @FXML
    private ComboBox<AnimalType> animalTypeComboBox;

    @FXML
    private TableView<Animal> animalsTable;

    @FXML
    private TableColumn<Animal, String> nameColumn;

    @FXML
    private TableColumn<Animal, String> typeColumn;

    @FXML
    private TableColumn<Animal, Void> actionsColumn;

    public void setServiceAndCenter(Service service, AdoptionCenter adoptionCenter) {
        this.service = service;
        this.adoptionCenter = adoptionCenter;
        initializeCenterDetails();
        initializeAnimalTypeComboBox();
        initializeAnimalTable();
        loadAnimals(null);
        ControllerRegistry.registerController(this);
    }

    private void initializeCenterDetails() {
        centerNameLabel.setText("Name: " + adoptionCenter.getName());
        locationLabel.setText("Location: " + adoptionCenter.getLocation());
        updatePercentageLabel();
    }

    private void initializeAnimalTypeComboBox() {
        animalTypeComboBox.setItems(FXCollections.observableArrayList(AnimalType.values()));
        animalTypeComboBox.getItems().add(null);
        animalTypeComboBox.setPromptText("Select Animal Type");
        animalTypeComboBox.setOnAction(event -> {
            AnimalType selectedType = animalTypeComboBox.getValue();
            loadAnimals(selectedType);
        });
    }

    private void initializeAnimalTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType().name()));
        addActionsColumn();
    }

    private void loadAnimals(AnimalType type) {
        List<Animal> animals;
        if (type == null) {
            animals = service.filterByAdoptionCenter(adoptionCenter.getId());
        } else {
            animals = service.filterByType(type).stream()
                    .filter(animal -> animal.getAdoptionCenterId().equals(adoptionCenter.getId()))
                    .toList();
        }
        System.out.println("Loaded animals for center: " + adoptionCenter.getName() + " -> " + animals.size());
        animalsTable.setItems(FXCollections.observableArrayList(animals));
    }


    private void addActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button requestTransferButton = new Button("Request Transfer");

            {
                requestTransferButton.setOnAction(event -> {
                    Animal animal = getTableView().getItems().get(getIndex());
                    requestTransfer(animal);
                });
                requestTransferButton.getStyleClass().add("request-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(requestTransferButton);
                }
            }
        });
    }

    @FXML
    private void requestTransfer(Animal animal) {
        List<AdoptionCenter> centersInSameLocation = service.getCentersInSameLocation(adoptionCenter.getLocation())
                .stream()
                .filter(center -> !center.getId().equals(adoptionCenter.getId()))
                .toList();

        for (AdoptionCenter center : centersInSameLocation) {
            service.createTransferRequest(adoptionCenter, center, animal);
            showNotification(center, animal);
        }
    }

    public void onClose() {
        ControllerRegistry.unregisterController(this);
    }

    private void showNotification(AdoptionCenter center, Animal animal) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Transfer Request");
        alert.setHeaderText(center.getName() + " requested to transfer " + animal.getName());
        alert.setContentText("Choose an option:");

        ButtonType acceptButton = new ButtonType("Accept");
        ButtonType ignoreButton = new ButtonType("Ignore");
        alert.getButtonTypes().setAll(acceptButton, ignoreButton);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == acceptButton) {
                service.transferAnimal(animal, center.getId());
                List<TransferRequests> requests = service.getTransferRequestsForAnimal(animal.getId());
                for (TransferRequests request : requests) {
                    if (request.getToCenterId().equals(center.getId())) {
                        service.acceptTransfer(request.getId());
                    } else {
                        service.ignoreTransfer(request.getId());
                    }
                }

                // Reîncarcă lista de animale pentru centrul curent
                loadAnimals(null);

                // Actualizează procentajul de ocupare
                updatePercentageLabel();
                ControllerRegistry.refreshAll();
                Alert confirmation = new Alert(Alert.AlertType.INFORMATION, "Transfer accepted successfully!");
                confirmation.showAndWait();
            } else if (buttonType == ignoreButton) {
                List<TransferRequests> requests = service.getTransferRequestsForAnimal(animal.getId());
                for (TransferRequests request : requests) {
                    if (request.getToCenterId().equals(center.getId())) {
                        service.ignoreTransfer(request.getId());
                    }
                }
            }
        });
    }
    public class ControllerRegistry {
        private static final List<AdoptionController> controllers = new ArrayList<>();

        public static void unregisterController(AdoptionController controller) {
            controllers.remove(controller);
        }

        public static void registerController(AdoptionController controller) {
            controllers.add(controller);
            System.out.println("Registered controller for center: " + controller.adoptionCenter.getName());
        }

        public static void refreshAll() {
            System.out.println("Refreshing all controllers: " + controllers.size());
            for (AdoptionController controller : controllers) {
                controller.loadAnimals(null);
                controller.updatePercentageLabel();
            }
        }

    }

}
