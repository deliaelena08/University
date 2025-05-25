package com.example.demo.SocialNetwork.Controller;
import com.example.demo.SocialNetwork.Model.Friends.FriendRequest;
import com.example.demo.SocialNetwork.Model.User;
import com.example.demo.SocialNetwork.Services.SocialNetworkService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class FriendRequestsController {
    User currentuser;
    private SocialNetworkService socialNetworkService;
    @FXML
    private TableView<FriendRequest> tableView;
    @FXML
    private TableColumn<FriendRequest, String> tableColumnSenderFirstName;
    @FXML
    private TableColumn<FriendRequest, String> tableColumnSenderLastName;
    //@FXML
    //private TableColumn<FriendRequest, String> tableColumnStatus;
    @FXML
    private TableColumn<FriendRequest, String> tableColumnTime;

    private ObservableList<FriendRequest> requestsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        tableColumnSenderFirstName.setCellValueFactory(new PropertyValueFactory<>("senderFirstName"));
        tableColumnSenderLastName.setCellValueFactory(new PropertyValueFactory<>("senderLastName"));
        //tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        tableView.setItems(requestsList);
    }

    public void setSocialNetworkService(SocialNetworkService service,User currentuser) {
        this.socialNetworkService = service;
        this.currentuser = currentuser;
        initModel();
    }

    private void initModel() {
        loadFriendsForSelectedUser(currentuser);
        tableView.setItems(requestsList);
    }

    @FXML
    public void loadFriendsForSelectedUser(User user) {
        List<FriendRequest> friendRequests = StreamSupport.stream(socialNetworkService.getFriendRequests().spliterator(), false)
                // hide other requests
                .filter(req -> req.getReceiver().getId().equals(user.getId()) && Objects.equals(req.getStatus(), "send"))
                .toList();
        requestsList.setAll(friendRequests);
        tableView.setItems(requestsList);
    }

    public void handleAcceptRequest() {
        updateStatus("accepted");
    }

    public void handleDeclineRequest() {
        updateStatus("declined");
    }

    private void updateStatus(String status) {
        FriendRequest selectedFriendRequest = tableView.getSelectionModel().getSelectedItem();
        if (selectedFriendRequest != null) {
            if (selectedFriendRequest.getReceiver().getId().equals(currentuser.getId())) {
                try {
                    socialNetworkService.updateRequestStatus(selectedFriendRequest, status);
                    requestsList.remove(selectedFriendRequest);
                    showMessage(Alert.AlertType.CONFIRMATION, "Friend " + status, "Friend request has been " + status + " successfully");
                } catch (Exception e) {
                    showMessage(Alert.AlertType.ERROR, "Error", "Error on updating request");
                }
            } else {
                showMessage(Alert.AlertType.WARNING, "Error", "How? How current user is not the same as the receiver?!!!");
            }
        } else {
            showMessage(Alert.AlertType.WARNING, "Error", "No request selected");
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
