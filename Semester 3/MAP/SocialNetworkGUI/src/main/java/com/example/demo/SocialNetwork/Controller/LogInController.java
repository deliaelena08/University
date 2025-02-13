package com.example.demo.SocialNetwork.Controller;

import com.example.demo.HelloApplication;
import com.example.demo.SocialNetwork.Model.User;
import com.example.demo.SocialNetwork.Services.ChatService;
import com.example.demo.SocialNetwork.Services.SocialNetworkService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController{
    private SocialNetworkService socialNetworkService;
    private ChatService chatService;
    private User user;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;

    public void setServices(SocialNetworkService socialNetworkService,ChatService chatService) {
        this.socialNetworkService = socialNetworkService;
        this.chatService = chatService;
    }

    @FXML
    public void initialize() {
        textFieldEmail.setText("");
        passwordField.setText("");
    }

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String email = textFieldEmail.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showMessage(Alert.AlertType.ERROR, "Error", "Your email or password is empty.");
            return;
        }
        User user = socialNetworkService.autentification(email, password);
        if (user != null) {
            setCurrentUser(user);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/profile-view.fxml"));
            AnchorPane profileLayout = fxmlLoader.load();
            ProfileController controller = fxmlLoader.getController();
            controller.setSocialNetworkService(socialNetworkService, user);

            Stage stage = new Stage();
            stage.setTitle("Profile");
            stage.setScene(new Scene(profileLayout));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Button) event.getSource()).getScene().getWindow());
            stage.show();
        } else {
            showMessage(Alert.AlertType.ERROR, "Error", "Authentication failed");
        }
    }
    @FXML
    public void handleSignup(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/signup-view.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        SignUpController controller = fxmlLoader.getController();
        controller.setServices(socialNetworkService,chatService);
        Stage stage = new Stage();
        stage.setTitle("Sign Up");
        stage.setScene(new Scene(userLayout));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Button) event.getSource()).getScene().getWindow());
        stage.show();
    }

    private void showMessage(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setCurrentUser(User user) {
        this.user = user;
    }
    private User getCurrentUser(){
        return user;
    }
}
