package org.example.adoptii.services;

import org.example.adoptii.models.AdoptionCenter;
import org.example.adoptii.models.Animal;
import org.example.adoptii.models.AnimalType;
import org.example.adoptii.models.TransferRequests;
import org.example.adoptii.repos.AnimalRepository;
import org.example.adoptii.repos.IRepository;

import java.util.List;
import java.util.stream.StreamSupport;

public class Service {
    IRepository<Integer, Animal> animalRepo;
    IRepository<Integer, AdoptionCenter> adoptionCenterRepo;
    IRepository<Integer, TransferRequests> transferRequestRepo;

    public Service(IRepository<Integer, Animal> animalRepo, IRepository<Integer, AdoptionCenter> adoptionCenterRepo, IRepository<Integer, TransferRequests> transferRequestRepo) {
        this.animalRepo = animalRepo;
        this.adoptionCenterRepo = adoptionCenterRepo;
        this.transferRequestRepo = transferRequestRepo;
    }

    public void addAnimal(String name, Integer adoptionCenterId, String type) {
        Animal animal = new Animal(name, adoptionCenterId, AnimalType.valueOf(type));
        animalRepo.save(animal);
    }

    public void addAdoptionCenter(String name, String location, Integer capacity) {
        AdoptionCenter adoptionCenter = new AdoptionCenter(name, location, capacity);
        adoptionCenterRepo.save(adoptionCenter);
    }

    public Iterable<Animal> getAnimals() {
        return animalRepo.findAll();
    }

    public Iterable<AdoptionCenter> getAdoptionCenters() {
        return adoptionCenterRepo.findAll();
    }

    public void updateAnimal(Integer id, String name, Integer adoptionCenterId, String type) {
        Animal animal = new Animal(name, adoptionCenterId, AnimalType.valueOf(type));
        animal.setId(id);
        animalRepo.update(animal);
    }

    public void updateAdoptionCenter(Integer id, String name, String location, Integer capacity) {
        AdoptionCenter adoptionCenter = new AdoptionCenter(name, location, capacity);
        adoptionCenter.setId(id);
        adoptionCenterRepo.update(adoptionCenter);
    }

    public Animal getAnimal(Integer id) {
        return animalRepo.find(id).orElse(null);
    }

    public AdoptionCenter getAdoptionCenter(Integer id) {
        return adoptionCenterRepo.find(id).orElse(null);
    }

    public List<Animal> filterByType(AnimalType type){
        return animalRepo.filterByType(type);
    }

    public List<Animal> filterByAdoptionCenter(Integer adoptionCenterId){
        return StreamSupport.stream(animalRepo.findAll().spliterator(), false)
                .filter(animal -> animal.getAdoptionCenterId().equals(adoptionCenterId))
                .toList();
    }

    public Double getProcentageOfAdoptionCenter(Integer adoptionCenterId){
        List<Animal> animals = filterByAdoptionCenter(adoptionCenterId);
        return (double) animals.size() / getAdoptionCenter(adoptionCenterId).getCapacity()*100;
    }

    public void requestTransfer(Integer animalId, Integer fromCenterId) {
        AdoptionCenter fromCenter = adoptionCenterRepo.find(fromCenterId).orElseThrow();
        List<AdoptionCenter> sameLocationCenters = StreamSupport.stream(adoptionCenterRepo.findAll().spliterator(), false)
                .filter(center -> center.getLocation().equals(fromCenter.getLocation()) && !center.getId().equals(fromCenterId))
                .toList();

        for (AdoptionCenter toCenter : sameLocationCenters) {
            TransferRequests request = new TransferRequests(animalId, fromCenterId, toCenter.getId(), "PENDING");
            transferRequestRepo.save(request);
        }
    }

    public void acceptTransfer(Integer requestId) {
        if (requestId == null) {
            throw new IllegalArgumentException("Request ID cannot be null");
        }

        TransferRequests request = transferRequestRepo.find(requestId).orElseThrow(() ->
                new IllegalArgumentException("Transfer request not found"));

        System.out.println("Found transfer request: " + request);

        Animal animal = animalRepo.find(request.getAnimalId()).orElseThrow(() ->
                new IllegalArgumentException("Animal not found"));

        System.out.println("Found animal: " + animal);
        animal.setAdoptionCenterId(request.getToCenterId());
        animalRepo.update(animal);
        System.out.println("Updated animal with new center ID: " + animal.getAdoptionCenterId());

        request.setStatus("ACCEPTED");
        transferRequestRepo.update(request);
        System.out.println("Updated transfer request to ACCEPTED");
    }



    public void ignoreTransfer(Integer requestId) {
        TransferRequests request = transferRequestRepo.find(requestId).orElseThrow();
        request.setStatus("IGNORED");
        transferRequestRepo.update(request);
    }
    public List<AdoptionCenter> getCentersInSameLocation(String location) {
        return StreamSupport.stream(adoptionCenterRepo.findAll().spliterator(), false)
                .filter(center -> center.getLocation().equals(location))
                .toList();
    }

    public void createTransferRequest(AdoptionCenter fromCenter, AdoptionCenter toCenter, Animal animal) {
        transferRequestRepo.save(new TransferRequests(animal.getId(), fromCenter.getId(), toCenter.getId(), "PENDING"));
    }

    public void transferAnimal(Animal animal, Integer newCenterId) {
        animal.setAdoptionCenterId(newCenterId);
        animalRepo.update(animal);
    }

    public List<TransferRequests> getTransferRequestsForAnimal(Integer animalId) {
       return transferRequestRepo.findByAnimalId(animalId);
    }

}
