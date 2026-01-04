package org.example.anar.services;

import org.example.anar.models.City;
import org.example.anar.models.River;
import org.example.anar.repos.IRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Service {
    IRepository<Integer, River> riverIRepository;
    IRepository<Integer, City> cityIRepository;

    public Service(IRepository<Integer, River> riverIRepository, IRepository<Integer, City> cityIRepository) {
        this.riverIRepository = riverIRepository;
        this.cityIRepository = cityIRepository;
    }

    public void addRiver(River river) {
        riverIRepository.save(river);
    }

    public void addCity(City city) {
        cityIRepository.save(city);
    }

    public List<River> getRivers() {
        return StreamSupport.stream(riverIRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }


    public Iterable<City> getCities() {
        return StreamSupport.stream(cityIRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void updateRiver(River river) {
        riverIRepository.update(river);
    }

    public void updateCity(City city) {
        cityIRepository.update(city);
    }

    public Map<String, List<String>> getCityByRiver() {
        Map<String,List<String>> cityByRiver = new HashMap<>();
        List<String> reduse = new ArrayList<>();
        List<String> medii = new ArrayList<>();
        List<String> majore = new ArrayList<>();
        Iterable<City> cities = cityIRepository.findAll();
        for (City city : cities) {
            River river = riverIRepository.find(city.getRiver()).get();
            if(city.getRiver()==river.getId()){
                if(river.getLength()< city.getCmdr()){
                    reduse.add(city.getName());
                }
                else if(river.getLength()>=city.getCmdr() && river.getLength()<=city.getCma()){
                    medii.add(city.getName());
                }
                else{
                    majore.add(city.getName());
                }
            }
        }
        cityByRiver.put("Risc redus",reduse);
        cityByRiver.put("Risc mediu",medii);
        cityByRiver.put("Risc major",majore);
        return cityByRiver;
    }
    public void updateRiverLevel(int riverId, int newLevel) {
        River river = riverIRepository.find(riverId).orElseThrow(() -> new IllegalArgumentException("River not found"));
        river.setLength(newLevel);
        riverIRepository.update(river);
        reevaluateCitiesRisk(); // Reorganizează localitățile în funcție de risc
    }

    private void reevaluateCitiesRisk() {
        Map<String, List<String>> cityByRiver = getCityByRiver();
        // Actualizare logică, notificări sau alte operațiuni necesare
        System.out.println("Riscurile localităților au fost actualizate.");
        System.out.println(cityByRiver);
    }
}
