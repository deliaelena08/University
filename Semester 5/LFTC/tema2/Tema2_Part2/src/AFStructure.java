import java.util.*;

public class AFStructure {
    public Set<String> stari = new HashSet<>();
    public Set<String> alfabet = new HashSet<>();
    public Map<String, Map<String, String>> tranzitii = new HashMap<>();
    public String stareInitiala;
    public Set<String> stariFinale = new HashSet<>();

    public AFStructure(Set<String> stari, Set<String> alfabet, String stareInitiala, Set<String> stariFinale, Map<String, Map<String, String>> tranzitii) {
        this.stari = stari;
        this.alfabet = alfabet;
        this.stareInitiala = stareInitiala;
        this.stariFinale = stariFinale;
        this.tranzitii = tranzitii;
    }

    public String prefixAcceptat(String secventa) {
        String stare = stareInitiala;
        StringBuilder prefix = new StringBuilder();
        StringBuilder celMaiLung = new StringBuilder();

        for (char c : secventa.toCharArray()) {
            String simbol = String.valueOf(c);

            if (!tranzitii.containsKey(stare) || !tranzitii.get(stare).containsKey(simbol)) {
                break;
            }

            stare = tranzitii.get(stare).get(simbol);
            prefix.append(simbol);

            if (stariFinale.contains(stare)) {
                celMaiLung.setLength(0);
                celMaiLung.append(prefix);
            }
        }
        return celMaiLung.toString();
    }
}