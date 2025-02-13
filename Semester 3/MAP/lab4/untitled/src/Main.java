//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class HelloWorld{
   public static void main(String[] args){
      //public-putem accesa de oriunde
      // void-nu se apeleaza nimic
      // static-nu e nevoie de o clasa ca sa actioneze comanda
       System.out.println("Hello World!");
       //System e clasa definita in java
      //out-output(clasa din pachet)
      //printl afiseaza o noua linie
      //I.Mostenire - class A extands class B
      //in java nu exista mostenire multipla
      //II.Incapsulare - le incarcam ca sa nu aiba toata lumea acces la ele (get/set),ascunderea detaliilor de implementare pentru clientul final
      //III.Abstractizare - clase abstracte si interfete (C++ Virtual - public interface ...)
      //IV.Polimorfism - abilitatea ca un obiect sa se poata comporta in diferite moduri
      //Clasa - o instanta, un template ; Obiect - o instanta a unei clase(new)
      //JAva nu are destructor, exista un Garbage Collector care se ocupa de ele

      Animal animal=new Animal(3,"Bichon Maltez");
      animal.printDetails();
   }
}

