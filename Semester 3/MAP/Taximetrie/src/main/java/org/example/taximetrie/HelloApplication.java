package org.example.taximetrie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.taximetrie.controllers.LoginController;
import org.example.taximetrie.models.Client;
import org.example.taximetrie.models.Comanda;
import org.example.taximetrie.models.Persoana;
import org.example.taximetrie.models.Sofer;
import org.example.taximetrie.repos.ClientRepository;
import org.example.taximetrie.repos.ComandaRepository;
import org.example.taximetrie.repos.PersoanaRepository;
import org.example.taximetrie.repos.SoferRepository;
import org.example.taximetrie.services.Service;
import org.example.taximetrie.validators.*;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String username="postgres";
        String password="Delia.08";
        String url="jdbc:postgresql://localhost:5432/Taximetrie";
        Validator<Sofer> soferValidator=(Validator<Sofer>)new SoferValidator();
        Validator<Persoana> persoanaValidator=(Validator<Persoana>)new PersoanaValidator();
        Validator<Comanda>  comandaValidator=(Validator<Comanda>)new ComandaValidator();
        Validator<Client> clientValidator=(Validator<Client>)new ClientValidator();
        SoferRepository soferRepository=new SoferRepository(url,username,password,soferValidator);
        PersoanaRepository persoanaRepository=new PersoanaRepository(url,username,password,persoanaValidator);
        ClientRepository clientRepository=new ClientRepository(url,username,password,clientValidator);
        ComandaRepository comandaRepository=new ComandaRepository(url,username,password,comandaValidator,persoanaRepository,soferRepository);
        Service service=new Service(soferRepository,persoanaRepository,comandaRepository,clientRepository);
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/org/example/taximetrie/views/login-view.fxml"));
        Scene scene = new Scene(loader.load());

        // Setează serviciul în MainController
        LoginController mainController = loader.getController();
        mainController.setService(service);
        scene.getStylesheets().add(getClass().getResource("/org/example/taximetrie/styles/styles.css").toExternalForm());
        // Configurare scenă principală
        stage.setTitle("Login PAGE");
        stage.setWidth(400);
        stage.setHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}