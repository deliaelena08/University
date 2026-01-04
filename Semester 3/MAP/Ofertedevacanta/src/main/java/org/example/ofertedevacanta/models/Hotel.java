package org.example.ofertedevacanta.models;

public class Hotel extends Entity<Double>{
    private double locationId;
    private String name;
    private int noRooms;
    private double pricePerNight;
    private Type type;

    public Hotel(double locationId, String name, int noRooms, double pricePerNight, Type type){
        this.locationId=locationId;
        this.name=name;
        this.noRooms=noRooms;
        this.pricePerNight=pricePerNight;
        this.type=type;
    }

    public double getLocationId(){
        return locationId;
    }

    public void setLocationId(double locationId){
        this.locationId=locationId;
    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public int getNoRooms(){
        return noRooms;
    }

    public void setNoRooms(int noRooms){
        this.noRooms=noRooms;
    }

    public double getPricePerNight(){
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight){
        this.pricePerNight=pricePerNight;
    }

    public Type getType(){
        return type;
    }

    public void setType(Type type){
        this.type=type;
    }

    @Override
    public String toString(){
        return "Hotel{" +"id="+getId()+","+
                "locationId=" + locationId + "," +
                "name='" + name + '\'' + "," +
                "noRooms=" + noRooms + "," +
                "pricePerNight=" + pricePerNight + "," +
                "type=" + type +
                '}';
    }

}
