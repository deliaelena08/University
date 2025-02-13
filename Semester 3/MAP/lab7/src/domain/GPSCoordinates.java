package domain;

public class GPSCoordinates {
    Double latitude;
    Double longitude;
    public GPSCoordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    //TODO define two fields latitude and longitude of type Double, constructor, getters, setters
}
