import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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

    private static String source;
    private static int currentIndex = 0;
    private static int currentLine = 1;

    private static final AFStructure AF_ID = AFDefinitions.getAFIdentifier();
    private static final AFStructure AF_INT = AFDefinitions.getAFIntegerConstant();
    private static final AFStructure AF_REAL = AFDefinitions.getAFRealConstant();


    private static int isStringConstant(int start) {
        if (start >= source.length() || source.charAt(start) != '"') return -1;

        for (int i = start + 1; i < source.length(); i++) {
            if (source.charAt(i) == '"') {
                return i - start + 1;
            }
        }
        return -1;
    }


    private static int isCharConstant(int start) {
        if (start >= source.length() || source.charAt(start) != '\'') return -1;

        if (start + 2 < source.length() && source.charAt(start + 2) == '\'') {
            return 3;
        }
        return -1;
    }


    private static void scanTokens(TS symbolTable, List<FIPEntry> fip) {
        while (currentIndex < source.length()) {

            while (currentIndex < source.length() && Character.isWhitespace(source.charAt(currentIndex))) {
                if (source.charAt(currentIndex) == '\n') {
                    currentLine++;
                }
                currentIndex++;
            }

            if (currentIndex >= source.length()) break;

            int longestMatch = 0;
            String token = null;
            String atomType = null;

            String remainingSource = source.substring(currentIndex);


            String matchID = AF_ID.prefixAcceptat(remainingSource);
            int matchLenID = matchID.length();
            if (matchLenID > longestMatch) {
                longestMatch = matchLenID;
                token = matchID;
                atomType = "id_or_keyword";
            }

            String matchReal = AF_REAL.prefixAcceptat(remainingSource);
            int matchLenReal = matchReal.length();
            if (matchLenReal > longestMatch) {
                longestMatch = matchLenReal;
                token = matchReal;
                atomType = "const";
            }

            String matchInt = AF_INT.prefixAcceptat(remainingSource);
            int matchLenInt = matchInt.length();
            if (matchLenInt > longestMatch) {
                longestMatch = matchLenInt;
                token = matchInt;
                atomType = "const";
            }

            int matchLenString = isStringConstant(currentIndex);
            if (matchLenString > longestMatch) {
                longestMatch = matchLenString;
                token = source.substring(currentIndex, currentIndex + longestMatch);
                atomType = "const";
            }
            int matchLenChar = isCharConstant(currentIndex);
            if (matchLenChar > longestMatch) {
                longestMatch = matchLenChar;
                token = source.substring(currentIndex, currentIndex + longestMatch);
                atomType = "const";
            }

            if (currentIndex + 1 < source.length()) {
                String potentialToken = source.substring(currentIndex, currentIndex + 2);
                if (OPERATORS.contains(potentialToken)) {
                    if (2 > longestMatch) {
                        longestMatch = 2;
                        token = potentialToken;
                        atomType = "operator";
                    }
                }
            }

            // f) Operatori/Separatori (1 caracter)
            if (longestMatch == 0 && currentIndex < source.length()) {
                String oneChar = source.substring(currentIndex, currentIndex + 1);
                if (OPERATORS.contains(oneChar)) {
                    longestMatch = 1;
                    token = oneChar;
                    atomType = "operator";
                } else if (SEPARATORS.contains(oneChar)) {
                    longestMatch = 1;
                    token = oneChar;
                    atomType = "separator";
                }
            }


            // 3. Procesarea Token-ului sau Raportarea Erorii

            if (token != null) {

                if (atomType.equals("id_or_keyword")) {
                    if (KEYWORDS.contains(token)) {
                        fip.add(new FIPEntry(token, token, null));
                    } else {
                        if (token.length() > 8) {
                            System.err.printf("Eroare lexicala: identificator '%s' depaseste 8 caractere (linia %d)%n", token, currentLine);
                        }
                        int pos = symbolTable.add(token);
                        fip.add(new FIPEntry(token, "ID", pos));
                    }
                } else if (atomType.equals("const")) {
                    int pos = symbolTable.add(token);
                    fip.add(new FIPEntry(token, "CONST", pos));
                } else {
                    fip.add(new FIPEntry(token, token, null));
                }

                currentIndex += longestMatch;
            } else {
                // Eroare: simbol necunoscut sau neînțeles
                char badChar = source.charAt(currentIndex);
                System.err.printf("Eroare lexicala: simbol necunoscut '%s' la linia %d%n", badChar, currentLine);

                currentIndex++;
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        source = Files.readString(Path.of("src/program1.mlp"));
        source = Files.readString(Path.of("src/program2.mlp"));
        TS symbolTable = new TS();
        List<FIPEntry> fip = new ArrayList<>();

        scanTokens(symbolTable, fip);

        System.out.println("\n=== FIP ===");
        System.out.println("Atom             | Cod Atom   | Index TS");
        System.out.println("-----------------|------------|---------");
        fip.forEach(System.out::println);

        System.out.println();
        symbolTable.print();

        Files.writeString(Path.of("src/FIP.txt"),
                fip.stream().map(Object::toString).reduce("Atom             | Cod Atom   | Index TS\n-----------------|------------|---------\n", (a, b) -> a + b + "\n"));
        symbolTable.saveToFile("src/TS.txt");
    }
}