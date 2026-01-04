package org.example.adoptii.models;

public class Animal extends Entity<Integer> {
    private String name;
    private Integer adoptionCenterId;
    private AnimalType type;

    public Animal(String name, Integer adoptionCenterId, AnimalType type) {
        this.name = name;
        this.adoptionCenterId = adoptionCenterId;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAdoptionCenterId() {
        return adoptionCenterId;
    }

    public void setAdoptionCenterId(Integer adoptionCenterId) {
        this.adoptionCenterId = adoptionCenterId;
    }

    public AnimalType getType() {
        return type;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", adoptionCenterId=" + adoptionCenterId +
                ", type=" + type +
                '}';
    }

}
