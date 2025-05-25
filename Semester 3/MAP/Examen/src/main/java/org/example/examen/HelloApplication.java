package org.example.examen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.examen.controllers.AddShowController;
import org.example.examen.controllers.UserViewController;
import org.example.examen.models.SeatReservations;
import org.example.examen.models.Show;
import org.example.examen.models.User;
import org.example.examen.repos.SeatReservationRepository;
import org.example.examen.repos.ShowRepository;
import org.example.examen.repos.UserRepository;
import org.example.examen.services.Service;
import org.example.examen.validators.*;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String username="postgres";
        String password="Delia.08";
        String url="jdbc:postgresql://localhost:5432/Spectacol_de_teatru";

        Validator<User> userValidator=(Validator<User>) new UserValidator();
        Validator<Show> showValidator= (Validator<Show>) new ShowValidator();
        Validator<SeatReservations> seatReservationsValidator=(Validator<SeatReservations>) new SeatReservationValidator();
        UserRepository userRepository=new UserRepository(url,username,password,userValidator);
        ShowRepository showRepository=new ShowRepository(url,username,password,showValidator);
        SeatReservationRepository seatReservationRepository=new SeatReservationRepository(url,username,password,seatReservationsValidator);
        Service service=new Service(showRepository,userRepository,seatReservationRepository);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/examen/views/add-show.fxml"));
        Scene scene = new Scene(loader.load());

        AddShowController controller = loader.getController();
        controller.setService(service);

        Stage centerStage = new Stage();
        centerStage.setTitle("Show management");
        centerStage.setScene(scene);
        centerStage.setHeight(600);
        centerStage.setWidth(500);
        centerStage.show();
        List<User> users = service.getAllUsers();
        for (User user : service.getAllUsers()) {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/org/example/examen/views/user-view.fxml"));
            Scene scene1 = new Scene(loader1.load());

            UserViewController controller1 = loader1.getController();
            controller1.setServiceAndUser(service, user.getEmail());

            Stage userStage = new Stage();
            userStage.setTitle("User: " + user.getName());
            userStage.setScene(scene1);
            userStage.setWidth(400);
            userStage.setHeight(500);
            userStage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}