package com.example.demo.SocialNetwork.Controller;
import com.example.demo.SocialNetwork.BCrypt.src.org.mindrot.jbcrypt.BCrypt;
import com.example.demo.SocialNetwork.Exceptions.ValidationException;
import com.example.demo.SocialNetwork.Services.ChatService;
import com.example.demo.SocialNetwork.Services.SocialNetworkService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpController {

    private SocialNetworkService socialNetworkService;
    private ChatService chatService;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button signupButton;

    public void setServices(SocialNetworkService socialNetworkService,ChatService chatService) {
        this.socialNetworkService = socialNetworkService;
        this.chatService = chatService;
    }

    @FXML
    public void initialize() {
        firstName.setText("");
        lastName.setText("");
        textFieldEmail.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    @FXML
    public void signupButtonClicked() {
        if(firstName.equals("") || lastName.equals("") || textFieldEmail.equals("") || passwordField.equals("")) {
           showMessage(Alert.AlertType.ERROR,"Error","Please fill all the fields");
        }
        else {
            if(passwordField.getText().equals( confirmPasswordField.getText())){
                try{
                    String hashedPassword = BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt());

                    socialNetworkService.addUser(firstName.getText(),lastName.getText(),textFieldEmail.getText(),hashedPassword);
                    showMessage(Alert.AlertType.CONFIRMATION,"SignUp","Sign up done successfully");
                    signupButton.getScene().getWindow().hide();
                }
                catch(ValidationException e){
                    showMessage(Alert.AlertType.ERROR,"Error",e.getMessage());
                }
             }
            else{
                showMessage(Alert.AlertType.ERROR,"Error","Passwords do not match");
            }
        }
    }

    private void showMessage(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
