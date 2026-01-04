package com.example.demo.SocialNetwork.Controller;

import com.example.demo.HelloApplication;
import com.example.demo.SocialNetwork.Model.User;
import com.example.demo.SocialNetwork.Services.SocialNetworkService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.image.Image;

import java.io.*;
import java.net.URL;

public class ProfileController {

    private User currentUser;
    private SocialNetworkService socialNetworkService;

    public void setSocialNetworkService(SocialNetworkService socialNetworkService,User currentUser) {
        this.socialNetworkService = socialNetworkService;
        this.currentUser = currentUser;
        loadpage();
    }
    @FXML
    private ImageView profileImage;
    @FXML
    private Button modifyDescriptionButton;
    @FXML
    private Button seeFriendsButton;
    @FXML
    private Button exitButton;
    @FXML
    private Text nameField;
    @FXML
    private Text emailField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Text friends;
    @FXML
    private Text friendsrequest;

    @FXML
    public void initialize() {
    }

    public void loadpage(){
        nameField.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        emailField.setText(currentUser.getEmail());
        friends.setText(Integer.toString(socialNetworkService.getFriends(currentUser.getId()).size()));
        friendsrequest.setText(Integer.toString(socialNetworkService.getNumberOfFriendRequests(currentUser.getId())));
        descriptionArea.setText(socialNetworkService.getUserProfile(currentUser.getId()).getDescription());
        descriptionArea.setEditable(false);
        try {
        String urlphoto = "src/main/resources/com/example/demo/images/" + socialNetworkService.getUserProfile(currentUser.getId()).getPhotoUrl().toString();
        File file = new File(urlphoto);
        InputStream imageStream = new FileInputStream(file);
        Image image = new Image(imageStream);
        if (profileImage==null) {
            System.out.println("Imaginea nu a fost găsită la calea: " + urlphoto);
        } else {
            profileImage.setImage(image);
        }}
        catch (FileNotFoundException e) {
            System.out.println("Nu exista imaginea: "+e.getMessage());
        }
    }

    @FXML
    public void handleModifyDescription(ActionEvent event) {
        descriptionArea.setEditable(true);
        descriptionArea.setStyle("-fx-border-color: blue;");
        modifyDescriptionButton.setText("Save Description");
        modifyDescriptionButton.setOnAction(e -> handleSaveDescription());
    }

    private void handleSaveDescription() {
        String newDescription = descriptionArea.getText();
        if (newDescription.isEmpty()) {
            showMessage(Alert.AlertType.WARNING, "Error", "Description cannot be empty.");
            return;
        }
        socialNetworkService.getUserProfile(currentUser.getId()).setDescription(newDescription);
        socialNetworkService.updateUserDescription(currentUser.getId(), newDescription);
        descriptionArea.setEditable(false);
        descriptionArea.setStyle(null);
        modifyDescriptionButton.setText("Modify Description");
        modifyDescriptionButton.setOnAction(this::handleModifyDescription);
        showMessage(Alert.AlertType.INFORMATION, "Success", "Description updated successfully.");
    }

    @FXML
    public void handleSeeFriends(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/friendship-view.fxml"));
        AnchorPane friendsLayout = fxmlLoader.load();

        FriendshipController controller = fxmlLoader.getController();
        controller.setSocialNetworkService(socialNetworkService, null, currentUser);
        Stage stage = new Stage();
        stage.setTitle("My Friends");
        stage.setScene(new Scene(friendsLayout));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Button) event.getSource()).getScene().getWindow());
        stage.show();
    }

    @FXML
    public void handleExit(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    private void showMessage(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
