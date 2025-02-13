package org.example.ofertedevacanta.models;

public class Client extends Entity<Double>{
    private String name;
    private Integer fidelityGrade;
    private Integer age;
    private Hobby hobby;

    public Client(String name, Integer fidelityGrade, Integer age, Hobby hobby){
        this.name=name;
        this.fidelityGrade=fidelityGrade;
        this.age=age;
        this.hobby=hobby;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public Integer getFidelityGrade(){
        return fidelityGrade;
    }

    public void setFidelityGrade(Integer fidelityGrade){
        this.fidelityGrade=fidelityGrade;
    }

    public Integer getAge(){
        return age;
    }

    public void setAge(Integer age){
        this.age=age;
    }

    public Hobby getHobby(){
        return hobby;
    }

    public void setHobby(Hobby hobby){
        this.hobby=hobby;
    }

    @Override
    public String toString(){
        return "Client{" +"id="+getId()+","+
                "name='" + name + '\'' +
                ", fidelityGrade=" + fidelityGrade +
                ", age=" + age +
                ", hobby=" + hobby +
                '}';
    }
}
