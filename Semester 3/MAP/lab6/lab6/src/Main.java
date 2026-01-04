import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static List<String> names() {
        return List.of("creioane","pix","Penar","aracet","caiet","culegere");
    }

    private static List<Dog> caini(){
       Dog caine1=new Dog("Husky",6);
       Dog caine2=new Dog("Akita",10);
       Dog caine3=new Dog("Bichon maltez",8);
       Dog caine4=new Dog("Samoyed",12);
       Dog caine5=new Dog("Shitzu",14);
       return List.of(caine1,caine2,caine3,caine4,caine5);
    }

    private static List<Dog> caini2(){
        Dog caine1=new Dog("Sara",6);
        Dog caine2=new Dog("Lulu",10);
        Dog caine4=new Dog("Simy",12);
        Dog caine5=new Dog("Luna",14);
        Dog caine6=new Dog("Sam",3);
        Dog caine7=new Dog("Boby",1);
        Dog caine8=new Dog("Teddy",7);
        return List.of(caine1,caine2,caine4,caine5,caine6,caine7,caine8);
    }

    public static List<Mall> malls(){
        Mall mall1=new Mall("Promenada","Andrei",2019,10000);
        Mall mall2=new Mall("Iulius","Eduard",2010,89000);
        Mall mall3=new Mall("Galeria","Cristian",2000,100);
        Mall mall4=new Mall("Shopping center","David",2000,1000);
        return List.of(mall1,mall2,mall3,mall4);
    }

    public static void main(String[] args) {
            List<String> cuvinte=names();
            System.out.println("Exercitiul 1");
            System.out.println(cuvinte);
            cuvinte = cuvinte.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
            System.out.println(cuvinte);
            System.out.println();

            System.out.println("Exercitiul 2a");
            List<Dog> caini=caini();
            caini.stream()
                    .filter(caine-> caine.getAge()>10)
                    .forEach(x->System.out.println(x));

            System.out.println("Exercitiul 2b");
            List<Dog> cainiSortati = caini.stream()
                .sorted(Comparator.comparing(Dog::getAge))
                .toList();
            cainiSortati.forEach(x->System.out.println(x.toString()));

            List<Dog> catelusi=caini2();
            System.out.println("Exercitiul 2c");
            catelusi.stream()
                    .filter(caine-> caine.getName().equals("Sam"))
                    .forEach(x->System.out.println(x.toString()));

            System.out.println("Exercitiul 2d");
            caini.stream().forEach(x->System.out.println(x.getName()));
            System.out.println();
            catelusi.stream().forEach(x->System.out.println(x.getName()));
            System.out.println();

            System.out.println("Exercitiul 3");
            List<Integer> numere=List.of(1,2,3,4,5,6);
            List<Integer> patrate=numere.stream()
                    .map(x->x*x)
                    .toList();
            patrate.forEach(System.out::println);

            System.out.println();
            System.out.println("Exercitiul 4");
            List<Mall> malls=malls();
            malls.forEach(m->System.out.println(m.toString()));
            System.out.println();
            List<Mall> triple = malls.stream()
                .map(mall -> {
                    Mall updatedMall = new Mall(mall.getName(), mall.getManager(),mall.getYear(),mall.getProfit() * 3);
                    return updatedMall;
                })
                .toList();
            System.out.println("Dupa triplarea profitului mall-urile sunt:");
            triple.forEach(m->System.out.println(m.toString()));
        }
    }
