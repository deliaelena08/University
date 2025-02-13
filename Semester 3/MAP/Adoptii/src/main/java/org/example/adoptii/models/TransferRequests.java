package org.example.adoptii.models;

public class TransferRequests extends Entity<Integer> {
    private Integer animalId;
    private Integer fromCenterId;
    private Integer toCenterId;
    private String status; // PENDING, ACCEPTED, IGNORED

    public TransferRequests(Integer animalId, Integer fromCenterId, Integer toCenterId, String status) {
        this.animalId = animalId;
        this.fromCenterId = fromCenterId;
        this.toCenterId = toCenterId;
        this.status = status;
    }

    public Integer getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Integer animalId) {
        this.animalId = animalId;
    }

    public Integer getFromCenterId() {
        return fromCenterId;
    }

    public void setFromCenterId(Integer fromCenterId) {
        this.fromCenterId = fromCenterId;
    }

    public Integer getToCenterId() {
        return toCenterId;
    }

    public void setToCenterId(Integer toCenterId) {
        this.toCenterId = toCenterId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
