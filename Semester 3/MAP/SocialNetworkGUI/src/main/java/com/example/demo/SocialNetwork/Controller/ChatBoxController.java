package com.example.demo.SocialNetwork.Controller;
import com.example.demo.SocialNetwork.Model.Message;
import com.example.demo.SocialNetwork.Model.User;
import com.example.demo.SocialNetwork.Services.ChatService;
import com.example.demo.SocialNetwork.Utils.Events.EventType;
import com.example.demo.SocialNetwork.Utils.Observer.Observer;
import com.example.demo.SocialNetwork.Utils.Events.SendMessageEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextAlignment;

public class ChatBoxController implements Observer<SendMessageEvent> {
    @FXML
    private ScrollPane scrollPaneChat;

    @FXML
    private VBox messageContainer;

    @FXML
    private TextField textFieldMessage;

    private ChatService service;
    private User currentUser;
    private User chatPartner;

    public void setChatData(ChatService service, User currentUser, User chatPartner) {
        this.service = service;
        this.service.addObserver(this);
        this.currentUser = currentUser;
        this.chatPartner = chatPartner;
        loadChatMessages();
    }

    private HBox getLabel(Message message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        Label label = new Label();
        label.setWrapText(true);

        if (message.getSender().equals(currentUser)) {
            label.setText("[You]: " + message.getMessage() + " (" + message.getTime().format(formatter) + ")");
            label.getStyleClass().add("message-sent");
            label.setTextAlignment(TextAlignment.RIGHT);
        } else {
            label.setText("[" + message.getSender().getFirstName() + "]: " + message.getMessage() + " (" + message.getTime().format(formatter) + ")");
            label.getStyleClass().add("message-received");
            label.setTextAlignment(TextAlignment.LEFT);
        }
        HBox messageBox = new HBox();
        messageBox.getChildren().add(label);

        if (message.getSender().equals(currentUser)) {
            messageBox.setAlignment(Pos.CENTER_RIGHT); // Mesajele trimise se aliniază la dreapta
        } else {
            messageBox.setAlignment(Pos.CENTER_LEFT); // Mesajele primite se aliniază la stânga
        }

        return messageBox;
        //return label;
    }


    private void loadChatMessages() {
        messageContainer.getChildren().clear();
        Iterable<Message> chatMessages = service.getChat(currentUser.getId(), chatPartner.getId());
        for (Message message : chatMessages) {
            HBox label=getLabel(message);
            messageContainer.getChildren().add(label);
        }
    }


    @FXML
    public void handleSendMessage(javafx.event.ActionEvent actionEvent) {
        String messageText = textFieldMessage.getText();
        if (!messageText.isEmpty()) {
            service.addMessage(currentUser.getId(), chatPartner.getId(), messageText);
            textFieldMessage.clear();
            scrollPaneChat.setVvalue(1.0);
        }
    }

    @Override
    public void update(SendMessageEvent event) {
        if (currentUser != null || chatPartner != null) {
            Message msg = event.getMessage();
            System.out.println("Update received: " + msg);
            System.out.println("Chat partner: " + chatPartner + ", Current user: " + currentUser);

            if((msg.getSender().equals(chatPartner) & msg.getReceiver().equals(currentUser)) || (msg.getSender().equals(currentUser) & msg.getReceiver().equals(chatPartner)) ) {
                if (event.getEvent() == EventType.SENDMESSAGE) {
                    HBox label = getLabel(msg);
                    messageContainer.getChildren().add(label);
                    scrollPaneChat.setVvalue(1.0);
                } else if (event.getEvent() == EventType.DELETEMESSAGE) {
                    messageContainer.getChildren().remove(msg);
                }
            }
        }
    }

    public void closeChat() {
        if (service != null) {
            service.removeObserver(this);
            System.out.println("Chat closed for: " + currentUser + " and " + chatPartner);
        }
    }

}
