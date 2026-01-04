package org.example.ofertedevacanta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ofertedevacanta.controllers.MainController;
import org.example.ofertedevacanta.repos.*;
import org.example.ofertedevacanta.services.Service;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        String username = "postgres";
        String password = "Delia.08";
        String url = "jdbc:postgresql://localhost:5432/Oferte_de_vacanta";

        // Inițializare repository-uri
        ClientRepository clientRepository = new ClientRepository(url, username, password);
        LocationRepository locationRepository = new LocationRepository(url, username, password);
        HotelRepository hotelRepository = new HotelRepository(url, username, password);
        SpecialOffersRepository specialOffersRepository = new SpecialOffersRepository(url, username, password);
        RservationRepository reservationRepository = new RservationRepository(url, username, password);

        // Creare serviciu
        Service service = new Service(locationRepository, specialOffersRepository, hotelRepository, clientRepository, reservationRepository);

        // Încărcare MainController și setare Service
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ofertedevacanta/views/main-view.fxml"));
        Scene scene = new Scene(loader.load());
        MainController mainController = loader.getController();
        mainController.setService(service);

        // Configurare fereastră principală
        stage.setTitle("Vacation Offers - Main Menu");
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
