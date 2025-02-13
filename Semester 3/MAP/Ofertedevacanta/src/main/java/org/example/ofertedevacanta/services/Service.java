package org.example.ofertedevacanta.services;

import org.example.ofertedevacanta.repos.IRepository;
import org.example.ofertedevacanta.models.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Service {
    IRepository<Double, Location> locationRepository;
    IRepository<Double,SpecialOffer> offerRepository;
    IRepository<Double,Hotel> hotelRepository;
    IRepository<Double,Client> clientRepository;
    IRepository<Double,Reservation>     reservationRepository;

    public Service(IRepository<Double, Location> locationRepository, IRepository<Double, SpecialOffer> offerRepository, IRepository<Double, Hotel> hotelRepository, IRepository<Double, Client> clientRepository, IRepository<Double, Reservation> reservationRepository) {
        this.locationRepository=locationRepository;
        this.offerRepository=offerRepository;
        this.hotelRepository=hotelRepository;
        this.clientRepository=clientRepository;
        this.reservationRepository=reservationRepository;
    }

    public void addLocation(Location location){
        locationRepository.save(location);
    }

    public void addOffer(SpecialOffer offer){
        offerRepository.save(offer);
    }

    public void addHotel(Hotel hotel){
        hotelRepository.save(hotel);
    }

    public void addClient(Client client){
        clientRepository.save(client);
    }

    public List<Location> getLocations(){
        List<Location> locations=new ArrayList<>();
        locationRepository.findAll().forEach(locations::add);
        return locations;
    }

    public List<SpecialOffer> getOffers(){
        List<SpecialOffer> offers=new ArrayList<>();
        offerRepository.findAll().forEach(offers::add);
        return offers;
    }

    public List<Hotel> getHotels(){
        List<Hotel> hotels=new ArrayList<>();
        hotelRepository.findAll().forEach(hotels::add);
        return hotels;
    }

    public List<Client> getClients(){
        List<Client> clients=new ArrayList<>();
        clientRepository.findAll().forEach(clients::add);
        return clients;
    }

    public Location getLocation(Double id){
        return locationRepository.find(id).orElse(null);
    }

    public SpecialOffer getOffer(Double id){
        return offerRepository.find(id).orElse(null);
    }

    public Hotel getHotel(Double id){
        return hotelRepository.find(id).orElse(null);
    }

    public Client getClient(Double id){
        return clientRepository.find(id).orElse(null);
    }

    public void updateLocation(Location location){
        locationRepository.update(location);
    }

    public void updateOffer(SpecialOffer offer){
        offerRepository.update(offer);
    }

    public void updateHotel(Hotel hotel){
        hotelRepository.update(hotel);
    }

    public void updateClient(Client client){
        clientRepository.update(client);
    }

    public List<Hotel> getHotelsByLocation(double idLocation){
        List<Hotel> hotels=new ArrayList<>();
        hotelRepository.findAll().forEach(hotel -> {
            if(hotel.getLocationId()==idLocation){
                hotels.add(hotel);
            }
        });
        return hotels;
    }

    public List<SpecialOffer> getOffersByHotelandPeriod(double idHotel, Date start, Date end){
        List<SpecialOffer> offers=new ArrayList<>();
        offerRepository.findAll().forEach(offer -> {
            if(offer.getHotelId()==idHotel && offer.getStartDate().compareTo(start)>=0 && offer.getEndDate().compareTo(end)<=0){
                offers.add(offer);
            }
        });
        return offers;
    }
    public void addReservation(double clientId, double hotelId, LocalDateTime startDate, int noNights) {
        Reservation reservation = new Reservation(clientId, hotelId, startDate, noNights);
        reservationRepository.save(reservation);
    }

    // Obține toate rezervările
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservationRepository.findAll().forEach(reservations::add);
        return reservations;
    }

    // Obține rezervările unui client specific
    public List<Reservation> getReservationsByClient(double clientId) {
        return getAllReservations().stream()
                .filter(reservation -> reservation.getClientId() == clientId)
                .collect(Collectors.toList());
    }

    // Obține clienți cu același hobby
    public List<Client> getClientsWithSameHobby(Hobby hobby) {
        return getClients().stream()
                .filter(client -> client.getHobby() == hobby)
                .collect(Collectors.toList());
    }

    // Notificare pentru clienții autentificați
    public void notifyClientsAboutReservation(Reservation reservation) {
        Hotel hotel = hotelRepository.find(reservation.getHotelId()).orElse(null);
        if (hotel != null) {
            Client reservingClient = clientRepository.find(reservation.getClientId()).orElse(null);
            if (reservingClient != null) {
                List<Client> sameHobbyClients = getClientsWithSameHobby(reservingClient.getHobby());
                for (Client client : sameHobbyClients) {
                    if (client.getId() != reservingClient.getId()) {
                        System.out.println("Notification: Client " + reservingClient.getName() +
                                " made a reservation at hotel " + hotel.getName() + ".");
                    }
                }
            }
        }
    }
}
