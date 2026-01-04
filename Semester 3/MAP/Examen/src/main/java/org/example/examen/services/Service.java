package org.example.examen.services;

import org.example.examen.models.Show;
import org.example.examen.models.User;
import org.example.examen.models.SeatReservations;
import org.example.examen.repos.IRepository;
import org.example.examen.repos.ShowRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {
    private final IRepository<Integer, Show> showRepository;
    private final IRepository<Integer, User> userRepository;
    private final IRepository<Integer, SeatReservations> seatReservationsRepository;

    public Service(IRepository<Integer, Show> showRepository, IRepository<Integer, User> userRepository, IRepository<Integer, SeatReservations> seatReservationsRepository) {
        this.showRepository = showRepository;
        this.userRepository = userRepository;
        this.seatReservationsRepository = seatReservationsRepository;
    }

    public void addShow(String name, int durationMinutes, LocalDateTime startDate, int numberOfSeats, LocalDateTime creationDate) {

        Show show = new Show(name, durationMinutes, startDate, numberOfSeats, creationDate);
        showRepository.save(show);
    }

    public void addUser(String email, String name) {
        User user = new User(email, name);
        userRepository.save(user);
    }

    public void addReservation(String userEmail, int showId) {
        SeatReservations reservation = new SeatReservations(userEmail, showId);
        seatReservationsRepository.save(reservation);
    }

    public List<Show> getAllShows() {
        return StreamSupport.stream(showRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<SeatReservations> getAllReservations() {
        return StreamSupport.stream(seatReservationsRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Show getShow(int id) {
        return showRepository.find(id).orElse(null);
    }

    public User getUser(int id) {
        return userRepository.find(id).orElse(null);
    }

    public SeatReservations getReservation(int id) {
        return seatReservationsRepository.find(id).orElse(null);
    }

    public void updateShow(int id, String name, int durationMinutes, String startDate, int numberOfSeats, String creationDate) {
        LocalDateTime startDateTime = LocalDateTime.parse(startDate);
        LocalDateTime creationDateTime = LocalDateTime.parse(creationDate);
        Show show = new Show(name, durationMinutes, startDateTime, numberOfSeats, creationDateTime);
        show.setId(id);
        showRepository.update(show);
    }

    public void updateUser(int id, String email, String name) {
        User user = new User(email, name);
        user.setId(id);
        userRepository.update(user);
    }

    public void updateReservation(int id, String userEmail, int showId) {
        SeatReservations reservation = new SeatReservations(userEmail, showId);
        reservation.setId(id);
        seatReservationsRepository.update(reservation);
    }

    public List<Show> getShowsByDay(String day) {
        return StreamSupport.stream(showRepository.findAll().spliterator(), false)
                .filter(show -> show.getStartDate().getDayOfWeek().toString().equalsIgnoreCase(day))
                .collect(Collectors.toList());
    }

    public List<Show> getShowsPaginatedAndSorted(int pageNumber, int pageSize) {
        return showRepository.findPaginatedAndSorted(pageNumber, pageSize);
    }

    public int getTotalShowPages(int size) {
        long totalShows = StreamSupport.stream(showRepository.findAll().spliterator(), false).count();
        return (int) Math.ceil((double) totalShows / size);
    }

    public void reserveSeat(String userEmail, int showId) {
        Show show = showRepository.find(showId).orElseThrow(() -> new IllegalArgumentException("Show not found"));
        if (show.getNumberOfSeats() <= 0) {
            throw new IllegalStateException("No seats available");
        }

        SeatReservations reservation = new SeatReservations(userEmail, showId);
        seatReservationsRepository.save(reservation);
        show.setNumberOfSeats(show.getNumberOfSeats() - 1);
        showRepository.update(show);
    }

    public List<Show> getUserReservationsOrderedByCreationDate(String userEmail) {
        return StreamSupport.stream(seatReservationsRepository.findAll().spliterator(), false)
                .filter(reservation -> reservation.getUserEmail().equals(userEmail))
                .map(reservation -> showRepository.find(reservation.getShowId()).orElse(null))
                .filter(show -> show != null)
                .sorted((show1, show2) -> show2.getCreationDate().compareTo(show1.getCreationDate())) // DESC
                .collect(Collectors.toList());
    }

    public List<Show> getReservedShowsForUser(String userEmail) {
        return StreamSupport.stream(seatReservationsRepository.findAll().spliterator(), false)
                .filter(reservation -> reservation.getUserEmail().equals(userEmail))
                .map(reservation -> showRepository.find(reservation.getShowId()).orElse(null))
                .filter(show -> show != null)
                .collect(Collectors.toList());
    }
    public String getOverlappingShowName(LocalDateTime startDate, int durationMinutes) {
        return ((ShowRepository) showRepository).getOverlappingShowName(startDate, durationMinutes);
    }

    public boolean hasUserReservedShow(String userEmail, int showId) {
        List<SeatReservations> reservations = StreamSupport.stream(seatReservationsRepository.findAll().spliterator(), false)
                .toList();

        return reservations.stream()
                .anyMatch(reservation -> reservation.getUserEmail().equals(userEmail) && reservation.getShowId() == showId);
    }



}
