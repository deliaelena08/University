import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TS {
    private final List<String> symbols = new ArrayList<>();

    public int add(String symbol) {
        int pos = symbols.indexOf(symbol);
        if (pos != -1)
            return pos;

        symbols.add(symbol);
        Collections.sort(symbols);
        return symbols.indexOf(symbol);
    }


    public void print() {
        System.out.println("Tabela de simboluri");
        for (int i = 0; i < symbols.size(); i++) {
            System.out.printf("%3d -> %s%n", i, symbols.get(i));
        }
    }

    public void saveToFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < symbols.size(); i++) {
            sb.append(i).append(" -> ").append(symbols.get(i)).append("\n");
        }
        Files.writeString(Path.of(filename), sb.toString());
    }
}