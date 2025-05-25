package org.example.examen.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.examen.models.Show;
import org.example.examen.services.Service;

import java.util.ArrayList;
import java.util.List;

public class UserViewController {

    private static final List<UserViewController> observers = new ArrayList<>();
    private Service service;
    private String userEmail;

    @FXML
    private Label userEmailLabel;

    @FXML
    private Pagination pagination;

    private final int pageSize = 3;

    public void setServiceAndUser(Service service, String userEmail) {
        this.service = service;
        this.userEmail = userEmail;
        userEmailLabel.setText("Email: " + userEmail);

        // Înregistrează controller-ul curent ca observator
        observers.add(this);

        setupPagination(); // Configurarea implicită
    }

    public static void notifyAllUsers() {
        for (UserViewController observer : observers) {
            observer.showNotification();
        }
    }

    public void showNotification() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText("New shows have been scheduled for user: " + userEmail + ". Do you want to see them?");

        // Adaugă butoanele Yes și No
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                setupPaginationDescendingByCreationDate(); // Configurăm pentru ordonare descrescătoare
            }
        });
    }


    private void setupPagination() {
        int totalShows = service.getTotalShowPages(pageSize);
        pagination.setPageCount(totalShows);

        pagination.setPageFactory(pageIndex -> {
            List<Show> shows = service.getShowsPaginatedAndSorted(pageIndex, pageSize);
            VBox pageContent = new VBox(10);

            shows.forEach(show -> {
                VBox showBox = new VBox(5);
                Label showLabel = new Label(show.getName() + " - " + show.getStartDate() + " - Seats: " + show.getNumberOfSeats());

                Button reserveButton = new Button("Reserve Seat");
                reserveButton.setDisable(service.hasUserReservedShow(userEmail, show.getId()) || show.getNumberOfSeats() == 0);

                reserveButton.setOnAction(event -> {
                    service.reserveSeat(userEmail, show.getId());
                    reserveButton.setDisable(true);
                    notifyObserversToUpdate();
                });

                showBox.getChildren().addAll(showLabel, reserveButton);
                pageContent.getChildren().add(showBox);
            });

            return pageContent;
        });
    }

    // Notifică toate ferestrele să se actualizeze
    private void notifyObserversToUpdate() {
        for (UserViewController observer : observers) {
            observer.updateSeatInfo();
        }
    }

    public void updateSeatInfo() {
        setupPagination();
    }

    private void setupPaginationDescendingByCreationDate() {
        List<Show> shows = service.getAllShows(); // Obținem toate spectacolele
        shows.sort((s1, s2) -> s2.getCreationDate().compareTo(s1.getCreationDate())); // Ordonăm descrescător

        int totalPages = (int) Math.ceil((double) shows.size() / pageSize);
        pagination.setPageCount(totalPages);

        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, shows.size());
            List<Show> pageShows = shows.subList(fromIndex, toIndex);

            VBox pageContent = new VBox();
            pageShows.forEach(show -> {
                Label showLabel = new Label(show.getName() + " - " + show.getCreationDate());
                pageContent.getChildren().add(showLabel);
            });
            return pageContent;
        });
    }
}
