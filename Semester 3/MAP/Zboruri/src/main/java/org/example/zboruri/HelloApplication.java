package org.example.zboruri;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.zboruri.controllers.FlightsController;
import org.example.zboruri.controllers.UserController;
import org.example.zboruri.repos.ClientsRepository;
import org.example.zboruri.repos.FlightRepository;
import org.example.zboruri.repos.TicketRepository;
import org.example.zboruri.service.Service;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String username = "postgres";
        String password = "Delia.08";
        String url = "jdbc:postgresql://localhost:5432/Zboruri";

        ClientsRepository clientsRepository = new ClientsRepository(url, username, password);
        FlightRepository flightRepository = new FlightRepository(url, username, password);
        TicketRepository ticketRepository = new TicketRepository(url, username, password);
        Service service = new Service(flightRepository, clientsRepository, ticketRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/zboruri/views/user-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        UserController userController = fxmlLoader.getController();
        userController.setService(service);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}