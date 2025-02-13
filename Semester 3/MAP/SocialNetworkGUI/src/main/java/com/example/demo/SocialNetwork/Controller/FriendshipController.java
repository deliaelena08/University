package com.example.demo.SocialNetwork.Controller;
import com.example.demo.HelloApplication;
import com.example.demo.SocialNetwork.Model.Friends.Friendship;
import com.example.demo.SocialNetwork.Model.Pages.Page;
import com.example.demo.SocialNetwork.Model.Pages.Pageable;
import com.example.demo.SocialNetwork.Model.User;
import com.example.demo.SocialNetwork.Services.ChatService;
import com.example.demo.SocialNetwork.Utils.Events.EventType;
import com.example.demo.SocialNetwork.Utils.Events.FriendshipChangeEvent;
import com.example.demo.SocialNetwork.Services.SocialNetworkService;
import com.example.demo.SocialNetwork.Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class FriendshipController implements Observer<FriendshipChangeEvent> {

    private SocialNetworkService socialNetworkService;
    private ChatService chatService;
    private ObservableList<User> friendsList = FXCollections.observableArrayList();
    private User currentUser;
    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, String> tableColumnFirstName;
    @FXML
    private TableColumn<User, String> tableColumnLastName;
    @FXML
    private TableColumn<User, String> tableColumnDesc;
    @FXML
    private  TextField textFieldAddUser;
    @FXML
    private Button buttonPrevPage;
    @FXML
    private Button buttonNextPage;

    private int currentPage = 0;
    private static final int PAGE_SIZE = 2;

    private ObservableList<User> model = FXCollections.observableArrayList();

    public void setSocialNetworkService(SocialNetworkService service,ChatService chatService,User currentUser) {
        this.socialNetworkService = service;
        this.socialNetworkService.addObserver(this);  // Abonare la evenimentele de prietenie
        this.chatService=chatService;
        this.currentUser=currentUser;
        initModel();
        loadFriendsForSelectedUser(currentUser);
    }

    private void initModel() {
        Iterable<User> users = socialNetworkService.getAllUsers();
        List<User> friendshipsList = StreamSupport.stream(users.spliterator(), false)
                .toList();
        model.setAll(friendshipsList);
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnDesc.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableView.setItems(friendsList);
        buttonPrevPage.setOnAction(event -> handlePrevPage());
        buttonNextPage.setOnAction(event -> handleNextPage());
    }


    @FXML
    public void handleAddFriend(ActionEvent event) {
        String email= textFieldAddUser.getText();
        if(!email.isEmpty()){
            User user=socialNetworkService.findUserByEmail(email);
            User currentUser = getCurrentUser();
            socialNetworkService.addRequest( currentUser.getId(),user.getId(),"send");
            showMessage(Alert.AlertType.CONFIRMATION, "Friend request", "Your friend request was sended");

        } else {
            showMessage(Alert.AlertType.ERROR, "Error", "Failed trying to send your friend request");
        }
    }


    @FXML
    public void handleDeleteFriend(ActionEvent event) {
        User selectedFriend = tableView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                try {
                    socialNetworkService.deleteFriendship(currentUser.getId(), selectedFriend.getId());
                    friendsList.remove(selectedFriend);
                    showMessage(Alert.AlertType.CONFIRMATION, "Friend deleted", "Friend deleted successfully");
                } catch (Exception e) {
                    showMessage(Alert.AlertType.ERROR, "Error", "Error deleting friend");
                }
            } else {
                showMessage(Alert.AlertType.ERROR, "Error", "user not found");
            }
        } else {
            showMessage(Alert.AlertType.WARNING, "Error", "No friend selected");
        }
    }

    @FXML
    public void handleFriendRequests(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/friendrequest-view.fxml"));
        AnchorPane userLayout = fxmlLoader.load();

        FriendRequestsController controller = fxmlLoader.getController();
        controller.setSocialNetworkService(socialNetworkService,getCurrentUser());

        Stage stage = new Stage();
        stage.setTitle("Friend Requests");
        stage.setScene(new Scene(userLayout));

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Button) event.getSource()).getScene().getWindow());
        stage.show();

    }

    private User getCurrentUser(){
        return currentUser;
    }

    @FXML
    public void handleChat(ActionEvent event) throws IOException {
       User selectedFriend = tableView.getSelectionModel().getSelectedItem();
        User currentUser =getCurrentUser();
        if(selectedFriend != null && currentUser != null){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/chat-view.fxml"));
            AnchorPane chatLayout = fxmlLoader.load();
            ChatBoxController controller = fxmlLoader.getController();
            controller.setChatData(chatService,currentUser,selectedFriend);
            Stage stage = new Stage();
            Scene scene = new Scene(chatLayout);
            scene.getStylesheets().add(getClass().getResource("/com/example/demo/css/chatstyle.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Chat with "+selectedFriend.getFirstName()+" "+selectedFriend.getLastName());
            stage.setScene(scene);
            stage.setOnCloseRequest(e -> controller.closeChat());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Button) event.getSource()).getScene().getWindow());
            stage.show();
        }else {
            showMessage(Alert.AlertType.WARNING, "Atention", "Select a friend!");
        }
    }

    @FXML
    public void loadFriendsForSelectedUser(User user) {
        Pageable pageable = new Pageable(PAGE_SIZE, currentPage);
        Page<User> friendsPage = socialNetworkService.getFriendsPaginated(user.getId(), pageable);
        ArrayList<User> friends=new ArrayList<>();
        friendsPage.getElementsOnPage().forEach(friends::add);
        friendsList.setAll(friends);
        updatePaginationButtons(friendsPage);
    }

    private void showMessage(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void update(FriendshipChangeEvent event) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            Friendship friendship = event.getFriendship();
            User otherUser = null;
            if (currentUser.getId() == friendship.getId().getE1().getId()) {
                otherUser = friendship.getId().getE2();
            } else {
                otherUser = friendship.getId().getE1();
            }
            if (event.getEventType() == EventType.ADD) {
                friendsList.add(otherUser);
            } else if (event.getEventType() == EventType.REMOVE) {
                friendsList.remove(otherUser);
            }
        }
    }
    private void handleNextPage() {
        currentPage++;
        loadFriendsForSelectedUser(currentUser);
    }

    private void handlePrevPage() {
        if (currentPage > 0) {
            currentPage--;
            loadFriendsForSelectedUser(currentUser);
        }
    }

    private void updatePaginationButtons(Page<User> page) {
        buttonPrevPage.setDisable(currentPage == 0);
        buttonNextPage.setDisable(currentPage >= (page.getTotalNrOfElements() - 1) / PAGE_SIZE);
    }
}