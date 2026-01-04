import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TS {
    private final List<String> symbols = new ArrayList<>();

    /**
     * Adauga simbolul in tabel, mentinand ordinea lexicografica
     * Daca simbolul exista deja, returneaza indexul lui
     */
    public int add(String symbol) {
        int pos = find(symbol);
        if (pos != -1)
            return pos;

        symbols.add(symbol);
        Collections.sort(symbols);
        return symbols.indexOf(symbol);
    }

    /**
     * Cauta simbolul si returneaza indexul lui sau -1 daca nu exista
     */
    public int find(String symbol) {
        return symbols.indexOf(symbol);
    }

    /**
     * Afiseaza tabela ordonata lexicografic
     */
    public void print() {
        System.out.println("Tabela de simboluri");
        for (int i = 0; i < symbols.size(); i++) {
            System.out.printf("%3d -> %s%n", i, symbols.get(i));
        }
    }

    /**
     * Salveaza tabela de simboluri intr-un fisier
     */
    public void saveToFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < symbols.size(); i++) {
            sb.append(i).append(" -> ").append(symbols.get(i)).append("\n");
        }
        Files.writeString(Path.of(filename), sb.toString());
    }
}
