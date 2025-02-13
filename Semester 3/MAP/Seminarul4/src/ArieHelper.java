import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ArieHelper {
    public static void main(String[] args) {
        List<Patrat> patratList = new ArrayList<>();
        patratList.add(new Patrat(2));
        patratList.add(new Patrat(3));
        patratList.add(new Patrat(4));
        List<Cerc> cercList = new ArrayList<>();
        cercList.add(new Cerc(4));
        cercList.add(new Cerc(2));
        // functii lambda, x inseamna un singur argument si e de tip Patrat
        //Arie<Patrat> ariePatrat = x->{return x.getLat()*x.getLat();};
        //Arie<Cerc> arieCerc = x->{return x.getRaza()*x.getRaza()*Math.PI;};
        Arie<Patrat> ariePatrat = x->x.getLat()*x.getLat();
        Arie<Cerc> arieCerc = x->x.getRaza()*x.getRaza()*Math.PI;
        // functie lambda
        System.out.println("\nAria pentru cele 3 patrate metoda I : ");
        patratList.forEach(x->System.out.println(ariePatrat.calcul(x)));
        System.out.println("\nAria pentru cele 2 cercuri metoda I : ");
        cercList.forEach(x->System.out.println(arieCerc.calcul(x)));
        System.out.println("\nAria pentru cele 3 patrate metoda II : ");
        Arie<Patrat> ariePatratII = ArieHelper::ariePatrat;
        Arie<Cerc> arieCercII =ArieHelper::arieCerc;
        patratList.forEach(x->System.out.println(ariePatrat.calcul(x)));
        System.out.println("\nAria pentru cele 2 cercuri metoda II : ");
        cercList.forEach(x->System.out.println(arieCercII.calcul(x)));

        System.out.println("\nCerinta 2: ");
        //Cerinta 2
        Predicate<String> incepeCuP=s->s.startsWith("p");
        List<String> lista= List.of("mere","pere","gutui","pepene","cirese","cartof","morcov");
        afiseazaCriteriu(lista, incepeCuP);

        //Cerinta 3
        System.out.println("\nCerinta 3a: ");
        String vocale="aeiouAEIOU";
        Predicate<Character> eVocala=x->{return vocale.contains(x.toString());};
        Function<String,Integer> converterLambda = x -> Integer.valueOf(x);
        System.out.println(converterLambda.apply("14"));
        Function<String,String> convertLimbaPasareasca=x->{
            String rez="";
            for(int i=0;i<x.length();i++){
                rez=rez+x.charAt(i);
                if(eVocala.test(x.charAt(i)))
                    rez+="p"+x.charAt(i);
            }
            return rez.toString();
        };
        System.out.println(convertLimbaPasareasca.apply("Mama merge la piata"));
        System.out.println("\nCerinta 3b: ");
        Function<Integer,Integer> converterMethodReference=Integer::valueOf;
        System.out.println(converterMethodReference.apply(3));

        //Cerinta 4
        //System.out.println("\nCerinta 4a: ");
       // Supplier<Integer> supplierLambda = () -> Integer.valueOf(42);

        //Cerinta 6
        System.out.println("\nCerinta 6a: ");
        List<String> list=List.of("asd","bce","asd","bcr","cc");
        Stream<String> stream=list.stream();
       stream.filter(x->x.startsWith("b"))
               .map(x->x.toUpperCase())
                   .forEach(System.out::println);

       System.out.println();

        /**stream.filter(x->{
            System.out.println(x);
            return x.startsWith("b");
        })
                .map(x->{
                    System.out.println(x);
                    return x.toUpperCase();
                })
                .forEach(System.out::println);
        **/
        List<Integer> intregi=List.of(1,2,3,4,5,6,7,3,4,7,2);
        Stream<Integer> stream2=intregi.stream();
        Integer rez = stream2.reduce(0,(a,b)-> {
            return a+b;
        });
        System.out.println("Suma a toate numerele din lista: "+rez);
    }

    public static double ariePatrat(Patrat patrat) {
        return patrat.getLat()*patrat.getLat();
    }

    public static double arieCerc(Cerc cerc) {
        return cerc.getRaza()*cerc.getRaza()*Math.PI;
    }

    public static <E> void afiseazaCriteriu(List<E> l,Predicate<E> p) {
       //metoda generica care afiseaza entitatile dintr-o lista, care satisfac un anumit
        // criteriu, specificat ca parametru de tip Predicate.
        l.forEach(el->
        {
            if(p.test(el))
                System.out.println(el);
        });
    }
}
