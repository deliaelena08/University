import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class LexicalAnalyzerMLP {
    private static final Set<String> KEYWORDS = Set.of(
            "main", "integer", "float", "char", "string", "bool",
            "read", "write", "if", "else", "while", "struct"
    );

    public static void main(String[] args) throws IOException {
        String source = Files.readString(Path.of("src/program.mlp"));

        Pattern tokenPattern = Pattern.compile(
                "<-|\\*\\*|[><=!]=?|[+\\-*/%]|[{}(),]|" + // operators,separators
                        "'[^']*'|\"[^\"]*\"|" +           // constants char or string
                        "\\d+(?:\\.\\d+)?|" +             // integer/real numbers
                        "[a-zA-Z_][a-zA-Z0-9_]*"          // identifiers, keywords
        );

        Matcher matcher = tokenPattern.matcher(source);
        int line = 1;
        int index = 0;

        System.out.println("List of atoms:\n");
        while (matcher.find()) {
            String token = matcher.group();
            String type;

            if (KEYWORDS.contains(token)) {
                type = "keyword";
            } else if (token.matches("\\d+(\\.\\d+)?")) {
                type = "number constant";
            } else if (token.matches("'[^']*'|\"[^\"]*\"")) {
                type = "string/char constant";
            } else if (token.matches("<-|\\*\\*|[><=!]=?|[+\\-*/%]")) {
                type = "operator";
            } else if (token.matches("[{}(),]")) {
                type = "separator";
            } else {
                type = "identifier";
            }

            System.out.printf("%-20s -> %s%n", token, type);

            while (index < matcher.start()) {
                if (source.charAt(index++) == '\n') line++;
            }
        }
    }
}
