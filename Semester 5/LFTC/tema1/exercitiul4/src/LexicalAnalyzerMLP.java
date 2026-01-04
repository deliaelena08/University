import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class LexicalAnalyzerMLP {

    private static final Set<String> KEYWORDS = Set.of(
            "main", "integer", "float", "char", "string", "bool",
            "read", "write", "if", "else", "while", "struct"
    );

    private static final Set<String> OPERATORS = Set.of(
            "<-", "+", "-", "*", "/", "%", "**", ">", "<", "=/", "=", "!"
    );

    private static final Set<String> SEPARATORS = Set.of(
            "{", "}", "(", ")", ","
    );


    private static final Map<String, Integer> ATOM_CODES = Map.ofEntries(
            Map.entry("ID", 0),
            Map.entry("CONST", 1),

            //Operatori
            Map.entry("<-", 2),
            Map.entry("+", 3),
            Map.entry("-", 4),
            Map.entry("*", 5),
            Map.entry("/", 6),
            Map.entry("%", 7),
            Map.entry("**", 8),

            //Comparatori
            Map.entry("<", 9),
            Map.entry(">", 10),
            Map.entry("=", 11),
            Map.entry("=/", 12),
            Map.entry("!", 13),

            //Separatoare
            Map.entry("{", 14),
            Map.entry("}", 15),
            Map.entry("(", 16),
            Map.entry(")", 17),
            Map.entry(",", 18),

            //Cuvinte cheie
            Map.entry("main", 19),
            Map.entry("integer", 20),
            Map.entry("float", 21),
            Map.entry("char", 22),
            Map.entry("string", 23),
            Map.entry("bool", 24),
            Map.entry("read", 25),
            Map.entry("write", 26),
            Map.entry("if", 27),
            Map.entry("else", 28),
            Map.entry("while", 29),
            Map.entry("struct", 30)
    );


    public static void main(String[] args) throws IOException {
        String source = Files.readString(Path.of("src/program.mlp"));

        TS symbolTable = new TS();
        List<FIPEntry> fip = new ArrayList<>();

        Pattern tokenPattern = Pattern.compile(
                "<-|\\*\\*|=/|[><=!]|[+\\-*/%]|[{}(),]|" +    // operators si separatori
                        "'[^']*'|\"[^\"]*\"|" +               // constante char/string
                        "\\d+(?:\\.\\d+)?|" +                 // constante numerice
                        "[a-zA-Z][a-zA-Z0-9_\\$#]*"           // identificatori
        );

        Matcher matcher = tokenPattern.matcher(source);
        int line = 1;
        int currentIndex = 0;

        while (matcher.find()) {
            String token = matcher.group();

            String between = source.substring(currentIndex, matcher.start());
            for (char c : between.toCharArray()) {
                if (c == '\n') line++;
            }

            if (!between.trim().isEmpty()) {
                for (char c : between.toCharArray()) {
                    if (!Character.isWhitespace(c)) {
                        if (!Character.isLetterOrDigit(c) &&
                                !SEPARATORS.contains(String.valueOf(c)) &&
                                !OPERATORS.contains(String.valueOf(c)) &&
                                c != '_' && c != '$' && c != '#' && c != '\'' && c != '\"') {

                            System.err.printf("Eroare lexicala: simbol necunoscut '%s' la linia %d%n", c, line);
                        }
                    }
                }
            }

            for (int i = currentIndex; i < matcher.start(); i++) {
                if (source.charAt(i) == '\n') line++;
            }
            currentIndex = matcher.end();
            if (KEYWORDS.contains(token)) {
                fip.add(new FIPEntry(token, "keyword", null));
            } else if (OPERATORS.contains(token)) {
                fip.add(new FIPEntry(token, "operator", null));
            } else if (SEPARATORS.contains(token)) {
                fip.add(new FIPEntry(token, "separator", null));
            } else if (token.matches("\\d+(\\.\\d+)?") || token.matches("'[^']*'|\"[^\"]*\"")) {
                int pos = symbolTable.add(token);
                fip.add(new FIPEntry(token, "const", pos));
            } else if (token.matches("[a-zA-Z][a-zA-Z0-9_\\$#]*")) {
                if (token.length() > 8) {
                    System.err.printf("Eroare lexicala: identificator '%s' depaseste 8 caractere (linia %d)%n", token, line);
                }
                int pos = symbolTable.add(token);
                fip.add(new FIPEntry(token, "id", pos));
            } else {
                System.err.printf("Eroare lexicala: simbol necunoscut '%s' la linia %d%n", token, line);
            }
        }

        System.out.println("\n=== FIP ===");
        fip.forEach(System.out::println);

        System.out.println();
        symbolTable.print();

        Files.writeString(Path.of("src/FIP.txt"),
                fip.stream().map(Object::toString).reduce("", (a, b) -> a + b + "\n"));
        symbolTable.saveToFile("src/TS.txt");
    }
}
