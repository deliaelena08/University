public class Animal {
    //tipuri primitive (cele din c++)
    //tipuri obiect (string + clase echivalente primitivelor)
    private int age;
    private String name;
    public Animal(int age,String name){
        this.age=age;//indica instanta curenta
        this.name=name;
    }

    //SETERE
    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    //GETERE
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    void printDetails(){
        System.out.println("Age: "+age+" Name: "+name);
    }
}