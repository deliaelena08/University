import java.io.*;
import java.util.*;

public class AutomatFinit {
    private Set<String> stari = new HashSet<>();
    private Set<String> alfabet = new HashSet<>();
    private Map<String, Map<String, String>> tranzitii = new HashMap<>();
    private String stareInitiala;
    private Set<String> stariFinale = new HashSet<>();

    public void citesteDinFisier(String numeFisier) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(numeFisier));
        String linie;
        boolean inTranzitii = false;

        while ((linie = br.readLine()) != null) {
            linie = linie.trim();
            if (linie.isEmpty()) continue;

            if (!inTranzitii) {
                if (linie.startsWith("Q")) {
                    stari.addAll(extrageElemente(linie));
                } else if (linie.startsWith("Σ")) {
                    alfabet.addAll(extrageElemente(linie));
                } else if (linie.startsWith("q0")) {
                    String[] parti = linie.split("=");
                    if (parti.length == 2) {
                        stareInitiala = parti[1].trim();
                    }
                } else if (linie.startsWith("F")) {
                    stariFinale.addAll(extrageElemente(linie));
                } else if (linie.startsWith("δ")) {
                    inTranzitii = true;
                }
            } else {
                // linie de forma: q0,+,- -> q2
                String[] parts = linie.split("->");
                if (parts.length != 2) continue;

                String stanga = parts[0].trim();
                String destinatie = parts[1].trim();

                String[] stangaParts = stanga.split(",");
                String stare = stangaParts[0].trim();
                tranzitii.putIfAbsent(stare, new HashMap<>());

                for (int i = 1; i < stangaParts.length; i++) {
                    String simbol = stangaParts[i].trim();
                    if (!simbol.isEmpty()) { // prevenim spatii si simboluri goale
                        tranzitii.get(stare).put(simbol, destinatie);
                    }
                }

            }
        }
        br.close();
    }


    private Set<String> extrageElemente(String linie) {
        int start = linie.indexOf("{");
        int end = linie.indexOf("}");
        String continut = linie.substring(start + 1, end);
        String[] elem = continut.split(",");
        Set<String> set = new HashSet<>();
        for (String e : elem) set.add(e.trim());
        return set;
    }

    public boolean verificaSecventa(String secventa) {
        String stare = stareInitiala;
        for (char c : secventa.toCharArray()) {
            String simbol = String.valueOf(c);
            if (!tranzitii.containsKey(stare)) return false;
            Map<String, String> t = tranzitii.get(stare);
            if (!t.containsKey(simbol)) return false;
            stare = t.get(simbol);
        }
        return stariFinale.contains(stare);
    }


    public String prefixAcceptat(String secventa) {
        String stare = stareInitiala;
        StringBuilder prefix = new StringBuilder();
        StringBuilder celMaiLung = new StringBuilder();

        for (char c : secventa.toCharArray()) {
            String simbol = String.valueOf(c);
            if (!tranzitii.containsKey(stare)) break;
            Map<String, String> t = tranzitii.get(stare);
            if (!t.containsKey(simbol)) break;

            stare = t.get(simbol);
            prefix.append(simbol);
            if (stariFinale.contains(stare)) {
                celMaiLung.setLength(0);
                celMaiLung.append(prefix);
            }
        }
        return celMaiLung.toString();
    }

    public void afiseaza() {
        System.out.println("Stari: " + stari);
        System.out.println("Alfabet: " + alfabet);
        System.out.println("Stare initiala: " + stareInitiala);
        System.out.println("Stari finale: " + stariFinale);
        System.out.println("Tranzitii:");

        for (var intrare : tranzitii.entrySet()) {
            String stare = intrare.getKey();
            Map<String, String> mapSimboluri = intrare.getValue();
            for (var simbolDest : mapSimboluri.entrySet()) {
                System.out.println(stare + ", " + simbolDest.getKey() + " -> " + simbolDest.getValue());
            }
        }
    }

    public void citesteDeLaTastatura() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Introdu multimea starilor:\n" + "ex: Q={q0,q1,...} \n");
        String l = sc.nextLine().trim();
        stari.addAll(extrageElemente(l));

        System.out.println("Introdu alfabetul:\n" +"ex: Σ={a,b,...} \n:");
        l = sc.nextLine().trim();
        alfabet.addAll(extrageElemente(l));

        System.out.println("Introdu starea initiala:\n" +  "ex: q0=q0 \n");
        l = sc.nextLine().trim();
        String[] parti = l.split("=");
        if (parti.length == 2) {
            stareInitiala = parti[1].trim();
        }

        System.out.println("Introdu multimea starilor finale:\n" + "ex: F={q0,q1,...} \n");
        l = sc.nextLine().trim();
        stariFinale.addAll(extrageElemente(l));

        System.out.println("Introdu tranzitiile cate o linie pe tranzitie (linia goala pentru terminare):\n" + "ex: q0,a,b -> q1 \n");
        while (true) {
            l = sc.nextLine().trim();
            if (l.isEmpty()) break;

            String[] parts = l.split("->");
            if (parts.length != 2) {
                System.out.println("Linie tranzitie invalida, ignorata: " + l);
                continue;
            }

            String stanga = parts[0].trim();
            String destinatie = parts[1].trim();

            String[] stangaParts = stanga.split(",");
            String stare = stangaParts[0].trim();
            tranzitii.putIfAbsent(stare, new HashMap<>());

            for (int i = 1; i < stangaParts.length; i++) {
                String simbol = stangaParts[i].trim();
                tranzitii.get(stare).put(simbol, destinatie);
            }
        }

        System.out.println("Citirea automatului de la tastatura s-a terminat. Mersi! <3");
    }


    public static void main(String[] args) throws IOException {
        AutomatFinit af = new AutomatFinit();

        System.out.println("Cum doresti sa citesti automatul finit?");
        System.out.println("1. Din fisier");
        System.out.println("2. De la tastatura");
        System.out.print("Optiune: ");

        Scanner sc = new Scanner(System.in);
        int mod = sc.nextInt(); sc.nextLine();

        if (mod == 1) {
            af.citesteDinFisier("src/automat.txt");
        } else {
            af.citesteDeLaTastatura();
        }
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("0.Iesire");
            System.out.println("1.Afisare automat finit");
            System.out.println("2.Verifica secventa daca este acceptata");
            System.out.println("3.Determina cel mai lung prefix");
            System.out.print("Optiune: ");
            int opt = sc.nextInt(); sc.nextLine();

            switch (opt) {
                case 0:
                    System.exit(0);
                case 1: {
                    af.afiseaza();
                    break;
                }
                case 2: {
                    System.out.print("Secventa: ");
                    String s = sc.nextLine();
                    System.out.println(af.verificaSecventa(s) ? "Acceptata" : "Respinsa");
                    break;
                }
                case 3: {
                    System.out.print("Introdu secventa: ");
                    String s = sc.nextLine();
                    String prefix = af.prefixAcceptat(s);

                    if (prefix.isEmpty()) {
                        System.out.println("Nu exista niciun prefix acceptat de automat.");
                    } else {
                        System.out.println("Cel mai lung prefix acceptat de automat este: \"" + prefix + "\"");
                    }
                    break;
                }
                default: return;
            }
        }
    }
}
