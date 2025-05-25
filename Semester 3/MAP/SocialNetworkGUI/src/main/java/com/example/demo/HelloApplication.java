package com.example.demo;
import com.example.demo.SocialNetwork.Controller.LogInController;
import com.example.demo.SocialNetwork.Model.Friends.Friendship;
import com.example.demo.SocialNetwork.Model.User;
import com.example.demo.SocialNetwork.Repos.DB.*;
import com.example.demo.SocialNetwork.Repos.Paging.PagingRepository;
import com.example.demo.SocialNetwork.Repos.Paging.UserPagingRepository;
import com.example.demo.SocialNetwork.Services.ChatService;
import com.example.demo.SocialNetwork.Services.SocialNetworkService;
import com.example.demo.SocialNetwork.Validations.FriendshipValidator;
import com.example.demo.SocialNetwork.Validations.UserValidator;
import com.example.demo.SocialNetwork.Validations.Validator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    SocialNetworkService service;
    ChatService chatService;

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Reading data from file");
        Validator<User> userValidator = (Validator<User>) new UserValidator();
        Validator<Friendship> friendshipValidator = (Validator<Friendship>) new FriendshipValidator();
        RepositoryUserDB userDB=new RepositoryUserDB(userValidator,"jdbc:postgresql://localhost:5432/User","postgres","Delia.08");
        RepositoryFriendshipDB friendshipDB=new RepositoryFriendshipDB(friendshipValidator,"jdbc:postgresql://localhost:5432/User","postgres","Delia.08",userDB);
        RepositoryFriendRequestDB requestDB= new RepositoryFriendRequestDB("jdbc:postgresql://localhost:5432/User","postgres","Delia.08",userDB);
        RepositoryMessagesDB messagesDB=new RepositoryMessagesDB("jdbc:postgresql://localhost:5432/User","postgres","Delia.08",userDB);
        UserPagingRepository pageRepo=new UserPagingRepository();
        RepositoryProfile repositoryProfile=new RepositoryProfile("jdbc:postgresql://localhost:5432/User","postgres","Delia.08");
        service =new SocialNetworkService(userDB,friendshipDB,requestDB,pageRepo,repositoryProfile);
        chatService = ChatService.getInstance(messagesDB, userDB);
        /*initView(stage);
        stage.setWidth(800);
        stage.show();*/
        initSecondView();
        initSecondView();
        //initSecondView();
    }
    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/login-view.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        LogInController userController = fxmlLoader.getController();
        userController.setServices(service,chatService);

    }

    private void initSecondView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/login-view.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        Stage secondStage = new Stage();
        secondStage.setScene(new Scene(userLayout));
        LogInController userController = fxmlLoader.getController();
        userController.setServices(service,chatService);
        secondStage.setTitle("Login");
        secondStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}