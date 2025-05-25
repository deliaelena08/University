package com.example.demo;

import com.example.demo.SocialNetwork.Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button btnHello;
    @FXML
    ListView<User> userList;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
        btnHello.setText("Hello");
        List<User> users = new ArrayList<>();
        ObservableList<User> observableList= FXCollections.observableList(users);
        userList.setItems(observableList);
    }
}