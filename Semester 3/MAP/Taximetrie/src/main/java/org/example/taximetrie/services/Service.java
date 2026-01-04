package org.example.taximetrie.services;

import org.example.taximetrie.models.Client;
import org.example.taximetrie.models.Comanda;
import org.example.taximetrie.models.Persoana;
import org.example.taximetrie.models.Sofer;
import org.example.taximetrie.repos.IRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    IRepository<Long, Sofer> soferRepository;
    IRepository<Long, Persoana> persoanaRepository;
    IRepository <Long, Comanda> comandaRepository;
    IRepository<Long, Client> clientRepository;

    public Service(IRepository<Long, Sofer> soferRepository, IRepository<Long, Persoana> persoanaRepository, IRepository<Long, Comanda> comandaRepository, IRepository<Long, Client> clientRepository) {
        this.soferRepository = soferRepository;
        this.persoanaRepository = persoanaRepository;
        this.comandaRepository = comandaRepository;
        this.clientRepository = clientRepository;
    }

    public void addSofer(String username, String name, String indicativMasina,Long persoanaID) {
        Sofer sofer = new Sofer(username, name, indicativMasina,persoanaID);
        soferRepository.save(sofer);
    }

    public void addPersoana(String username, String name) {
        Persoana persoana = new Persoana(username, name);
        persoanaRepository.save(persoana);
    }

    public Comanda addComanda(Client client, Sofer sofer, LocalDateTime data) {
        Comanda comanda = new Comanda(client, sofer, data);
        comandaRepository.save(comanda);
        return comanda;
    }

    public List<Sofer> getSoferi() {
        Iterable<Sofer> iterable = soferRepository.findAll();
        List<Sofer> soferi = new ArrayList<>();
        iterable.forEach(soferi::add);
        return soferi;
    }

    public List<Persoana> getPersoane() {
        Iterable<Persoana> iterable = persoanaRepository.findAll();
        List<Persoana> persoane = new ArrayList<>();
        iterable.forEach(persoane::add);

        System.out.println("Persoane din baza de date:");
        for (Persoana persoana : persoane) {
            System.out.println(persoana.getClass().getSimpleName() + ": " + persoana.getUsername());
        }

        return persoane;
    }


    public List<Comanda> getComenzi() {
        Iterable<Comanda> iterable = comandaRepository.findAll();
        List<Comanda> comenzi = new ArrayList<>();
        iterable.forEach(comenzi::add);
        return comenzi;
    }

    public Comanda findComandaById(Long id) {
        return comandaRepository.find(id).orElse(null);
    }

    public Sofer findSoferById(Long id) {
        return soferRepository.find(id).orElse(null);
    }

    public Persoana findPersoanaById(Long id) {
        return persoanaRepository.find(id).orElse(null);
    }

    public Comanda update(Comanda comanda) {
        return comandaRepository.update(comanda).orElse(null);
    }

    public Sofer update(Sofer sofer) {
        return soferRepository.update(sofer).orElse(null);
    }

    public Persoana update(Persoana persoana) {
        return persoanaRepository.update(persoana).orElse(null);
    }
    public List<Sofer> findSoferiByLocation(String locatie) {
        return getSoferi().stream()
                .filter(sofer -> sofer.getIndicativMasina().contains(locatie))
                .collect(Collectors.toList());
    }

    public void deleteComanda(Long id) {
        comandaRepository.find(id).ifPresent(comanda -> {
            comandaRepository.update(comanda);
        });
    }

    public List<Client> findClientsForSofer(Long soferId) {
        return getComenzi().stream()
                .peek(comanda -> System.out.println("Verific comanda: " + comanda))
                .filter(comanda -> comanda.getSofer() != null && comanda.getSofer().getId()==soferId)
                .peek(comanda -> System.out.println("Comanda validă pentru șofer: " + comanda))
                .map(Comanda::getClient)
                .distinct()
                .collect(Collectors.toList());
    }


    public List<Comanda> findComenziByDate(Long soferId, LocalDateTime date) {
        return getComenzi().stream()
                .filter(comanda -> comanda.getSofer().getId().equals(soferId) &&
                        comanda.getData().toLocalDate().equals(date.toLocalDate()))
                .collect(Collectors.toList());
    }
    public double findAverageComenziPerDay(Long soferId) {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        List<Comanda> recentComenzi = getComenzi().stream()
                .filter(comanda -> comanda.getSofer().getId().equals(soferId) &&
                        comanda.getData().isAfter(threeMonthsAgo))
                .collect(Collectors.toList());

        long zileDistincte = recentComenzi.stream()
                .map(comanda -> comanda.getData().toLocalDate())
                .distinct()
                .count();

        return zileDistincte == 0 ? 0 : (double) recentComenzi.size() / zileDistincte;
    }
    public Client findMostLoyalClient(Long soferId) {
        return getComenzi().stream()
                .filter(comanda -> comanda.getSofer().getId().equals(soferId))
                .collect(Collectors.groupingBy(Comanda::getClient, Collectors.counting()))
                .entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .map(entry -> entry.getKey())
                .orElse(null);
    }
    public void addClient(String username, String name) {
        Client client = new Client(username, name);
        clientRepository.save(client);
    }

    public List<Client> getClients() {
        Iterable<Client> iterable = clientRepository.findAll();
        List<Client> clients = new ArrayList<>();
        iterable.forEach(clients::add);
        return clients;
    }


}
