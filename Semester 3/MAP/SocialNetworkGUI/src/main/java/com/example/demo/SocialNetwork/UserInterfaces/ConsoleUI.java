package com.example.demo.SocialNetwork.UserInterfaces;

import com.example.demo.SocialNetwork.Services.SocialNetworkService;
import java.util.Scanner;

public class ConsoleUI {
    SocialNetworkService service;
    Scanner scanner = new Scanner(System.in);
    public ConsoleUI(SocialNetworkService service) {
        this.service = service;
    }

    public void start(){
        while(true){
            System.out.print("______MENU______ \n");
            System.out.println("1. Adauga utilizator");
            System.out.println("2. Sterge utilizator");
            System.out.println("3. Creaza o relatie de prietenie");
            System.out.println("4. Sterge o relatie de prietenie");
            System.out.println("5. Numărul de comunități");
            System.out.println("6. Cea mai sociabilă comunitate");
            System.out.println("7. Vizualizam toti utilizatorii");
            System.out.println("8. Vizualizam toate prieteniile");
            System.out.println("9. Iesi");
            System.out.println( "Introdu optiunea dorita: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consumăm newline
           switch(option){
               case 1:
                   addUser();
                   break;
               case 2:
                   deleteUser();
                   break;
               case 3:
                   addFriendship();
                   break;
               case 4:
                   deleteFriendship();
                   break;
               case 5:
                   numberOfComunities();
                   break;
               case 6:
                   mostSociableComunity();
                   break;
               case 7:
                   allUsers();
                   break;
               case 8:
                   allFriendships();
                   break;
               case 9:
                   System.out.println("Iesire...");
                   System.exit(0);
                   break;
               default:
                   System.out.println("Opțiune invalidă. Te rog sa reincerci");

           }
        }
    }
    void addUser(){
        System.out.print("Introduceți prenumele utilizatorului: ");
        String firstName = scanner.nextLine();
        System.out.print("Introduceți numele utilizatorului: ");
        String lastName = scanner.nextLine();
        System.out.print("Introduceți email-ul utilizatorului: ");
        String email = scanner.nextLine();
        System.out.print("Introduceți parola utilizatorului: ");
        String password = scanner.nextLine();
        try{
            service.addUser(firstName, lastName, email, password);
            System.out.println("Utilizator adaugat cu succes");
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    void deleteUser(){
        System.out.println("Introduceti Id-ul utilizatorului pe care doriti sa-l stergeti:");
        Long id = scanner.nextLong();
        try {
            service.deleteUser(id);
            System.out.println("Utilizator sters cu succes");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    void addFriendship(){
        System.out.println("Introduceti id-ul primului utilizator: ");
        Long id1 = scanner.nextLong();
        System.out.println("Introduceti id-ul pentru al doilea utilizaror: ");
        Long id2 = scanner.nextLong();
        try{
            service.addFriendship(id1, id2);
            System.out.println("Prietenie creata cu succes");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    void deleteFriendship(){
        System.out.println("Introduceti id-ul primului utilizator pe care vreti sa-l stergeti: ");
        Long id1 = scanner.nextLong();
        System.out.println("Introduceti id-ul pentru al doilea utilizatorului pe care vreti sa-l stergeti: ");
        Long id2 = scanner.nextLong();
        try {
            service.deleteFriendship(id1, id2);
            System.out.println("Prietenie stearsa cu succes");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    void numberOfComunities(){
        System.out.println("Numarul de comunitati existente este: "+service.getComunitiesnumber());
    }

    void mostSociableComunity(){
        System.out.println("Cea mai sociabila comunitate are este alcatuita din: ");
        service.mostSociableComunity().forEach(u->System.out.println(u.getFirstName()+" "+u.getLastName()+" | "));
    }

    void allUsers(){
        service.getAllUsers().forEach(u->System.out.println(u.getId()+" "+u.getFirstName()+" "+u.getLastName()));

    }

    void allFriendships(){
        service.getAllFriendships().forEach(f->System.out.println(f.getId().getE1().getFirstName()+" "+f.getId().getE1().getLastName()+" | "+f.getId().getE2().getFirstName()+" "+f.getId().getE2().getLastName()));
    }
}
