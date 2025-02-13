package org.example.zboruri.service;

import org.example.zboruri.models.Client;
import org.example.zboruri.models.Flight;
import org.example.zboruri.models.Ticket;
import org.example.zboruri.repos.IRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {
    IRepository<Integer, Flight> flightRepository;
    IRepository<Integer, Client> clientRepository;
    IRepository<Integer, Ticket> ticketRepository;

    public Service(IRepository<Integer, Flight> flightRepository, IRepository<Integer, Client> clientRepository, IRepository<Integer, Ticket> ticketRepository) {
        this.flightRepository = flightRepository;
        this.clientRepository = clientRepository;
        this.ticketRepository = ticketRepository;
    }

    public void addFlight(String from, String to, String departureTime, String landingTime, int seats) {
        LocalDateTime departureTime1 = LocalDateTime.parse(departureTime);
        LocalDateTime landingTime1 = LocalDateTime.parse(landingTime);
        Flight flight = new Flight(from, to, departureTime1, landingTime1, seats);
        flightRepository.save(flight);
    }

    public void addClient(String username, String name) {
        Client client = new Client(username, name);
        clientRepository.save(client);
    }

    public void addTicket(String username, int flightId, String purchaseTime) {
        LocalDateTime purchaseTime1 = LocalDateTime.parse(purchaseTime);
        Ticket ticket = new Ticket(username, flightId, purchaseTime1);
        ticketRepository.save(ticket);
    }

    public List<Flight> getAllFlights() {
        return StreamSupport.stream(flightRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Client> getAllClients() {
        return StreamSupport.stream(clientRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }


    public Iterable<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Flight getFlight(int id) {
        return flightRepository.find(id).orElse(null);
    }

    public Client getClient(int id) {
        return clientRepository.find(id).orElse(null);
    }

    public Ticket getTicket(int id) {
        return ticketRepository.find(id).orElse(null);
    }
    public void updateFlight(int id, String from, String to, String departureTime, String landingTime, int seats) {
        LocalDateTime departureTime1 = LocalDateTime.parse(departureTime);
        LocalDateTime landingTime1 = LocalDateTime.parse(landingTime);
        Flight flight = new Flight(from, to, departureTime1, landingTime1, seats);
        flight.setId(id);
        flightRepository.update(flight);
    }

    public void updateClient(int id, String username, String name) {
        Client client = new Client(username, name);
        client.setId(id);
        clientRepository.update(client);
    }

    public void updateTicket(int id, String username, int flightId, String purchaseTime) {
        LocalDateTime purchaseTime1 = LocalDateTime.parse(purchaseTime);
        Ticket ticket = new Ticket(username, flightId, purchaseTime1);
        ticket.setId(id);
        ticketRepository.update(ticket);
    }

    public List<Flight> getflightsbyday(String day){
        return StreamSupport.stream(flightRepository.findAll().spliterator(), false)
                .filter(flight -> flight.getDepartureTime().getDayOfWeek().toString().equals(day))
                .toList();
    }

    public List<Flight> getflightsbydayandcity(String from,String to,String day){
        return StreamSupport.stream(flightRepository.findAll().spliterator(), false)
                .filter(flight -> flight.getFrom().equals(from) && flight.getTo().equals(to) && flight.getDepartureTime().getDayOfWeek().toString().equals(day))
                .toList();
    }

    public void purchaseTicket(String username, int flightId) {
        Flight flight = flightRepository.find(flightId).orElseThrow(() -> new IllegalArgumentException("Flight not found"));
        if (flight.getSeats() <= 0) {
            throw new IllegalStateException("No seats available");
        }

        Ticket ticket = new Ticket(username, flightId, LocalDateTime.now());
        ticketRepository.save(ticket);
        flight.setSeats(flight.getSeats() - 1);
        flightRepository.update(flight);
    }

    public List<Flight> getFlightsPaginated(int page, int size) {
        List<Flight> allFlights = StreamSupport.stream(flightRepository.findAll().spliterator(), false)
                .toList();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, allFlights.size());
        return allFlights.subList(fromIndex, toIndex);
    }

    public int getTotalPages(int size) {
        long totalFlights = StreamSupport.stream(flightRepository.findAll().spliterator(), false).count();
        return (int) Math.ceil((double) totalFlights / size);
    }


}
