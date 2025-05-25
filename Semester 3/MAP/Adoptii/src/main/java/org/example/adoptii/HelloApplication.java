package org.example.adoptii;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.adoptii.controllers.AdoptionController;
import org.example.adoptii.models.AdoptionCenter;
import org.example.adoptii.models.Animal;
import org.example.adoptii.repos.AdoptionCenterRepository;
import org.example.adoptii.repos.AnimalRepository;
import org.example.adoptii.repos.TransferRequestRepository;
import org.example.adoptii.services.Service;
import org.example.adoptii.validators.AdoptionCenterValidator;
import org.example.adoptii.validators.AnimalValidators;
import org.example.adoptii.validators.Validator;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Validator<AdoptionCenter> adoptionCenterValidator=(Validator<AdoptionCenter>) new AdoptionCenterValidator();
        Validator<Animal> animalValidator=(Validator<Animal>) new AnimalValidators();
        String username="postgres";
        String password="Delia.08";
        String url="jdbc:postgresql://localhost:5432/Adoptie";
        AnimalRepository animalRepository = new AnimalRepository(animalValidator, url, username, password);
        AdoptionCenterRepository adoptionCenterRepository = new AdoptionCenterRepository(adoptionCenterValidator, url, username, password);
        TransferRequestRepository transferRequestRepository = new TransferRequestRepository(url, username, password);
        Service service=new Service(animalRepository, adoptionCenterRepository, transferRequestRepository);
        for (AdoptionCenter center : service.getAdoptionCenters()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/adoption-view.fxml"));
            Scene scene = new Scene(loader.load());

            AdoptionController controller = loader.getController();
            controller.setServiceAndCenter(service, center);

            Stage centerStage = new Stage();
            centerStage.setTitle(center.getName());
            centerStage.setScene(scene);
            centerStage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}