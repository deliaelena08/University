public class Dog {
    public String name;
    public int age;
    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }
     public void setAge(int age) {
        this.age = age;
     }
     public int getAge() {
        return age;
     }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
     }

    @Override
    public String toString() {
        return "Caine{" +
                "nume='" + name + '\'' +
                ", varsta='" + age + '\'' +
                '}';
    }
}
