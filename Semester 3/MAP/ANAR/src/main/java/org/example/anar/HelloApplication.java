package org.example.anar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.anar.controllers.CitiesController;
import org.example.anar.controllers.RiversController;
import org.example.anar.models.City;
import org.example.anar.models.River;
import org.example.anar.repos.CityRepository;
import org.example.anar.repos.RiverRepository;
import org.example.anar.services.Service;
import org.example.anar.validations.CityValidator;
import org.example.anar.validations.RiverValidator;
import org.example.anar.validations.Validator;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String username="postgres";
        String password="Delia.08";
        String url="jdbc:postgresql://localhost:5432/ANAR";
        Validator<River> riverValidator = (Validator<River>) new RiverValidator();
        Validator<City> cityValidator = (Validator<City>) new CityValidator();
        RiverRepository riverRepository = new RiverRepository(riverValidator, url, username, password);
        CityRepository cityRepository = new CityRepository(cityValidator, url, username, password);
        Service service = new Service(riverRepository, cityRepository);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/anar/views/rivers-view.fxml"));
        Scene scene = new Scene(loader.load());
        RiversController controller = loader.getController();
        controller.setService(service);
        stage.setTitle("Rivers");
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();

        FXMLLoader citiesLoader = new FXMLLoader(getClass().getResource("/org/example/anar/views/city-view.fxml"));
        Scene citiesScene = new Scene(citiesLoader.load());
        CitiesController cityController = citiesLoader.getController();
        cityController.setService(service);
        Stage citiesStage = new Stage();
        citiesStage.setTitle("City Warnings");
        citiesStage.setScene(citiesScene);
        citiesStage.setWidth(800);
        citiesStage.setHeight(600);
        citiesStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}