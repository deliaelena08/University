package org.example.ofertedevacanta.models;

public class Location extends Entity<Double>{
    private String name;
    public Location(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    @Override
    public String toString(){
        return "Location{" +"id="+getId()+","+
                "name='" + name + '\'' +
                '}';
    }
}
