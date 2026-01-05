import java.io.*;
import java.util.*;

public class LL1Parser {

    public static final String EPSILON = "EPSILON";
    public static final String END_MARKER = "$";

    static class Token {
        String type;
        String value;
        int line;

        public Token(String type, String value, int line) {
            this.type = type;
            this.value = value;
            this.line = line;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    static class Lexer {
        private String input;
        private int pos = 0;
        private int line = 1;
        private List<Token> tokens = new ArrayList<>();

        private static final Map<String, String> KEYWORDS = new HashMap<>();
        static {
            KEYWORDS.put("main", "KW_MAIN");
            KEYWORDS.put("if", "if");
            KEYWORDS.put("else", "else");
            KEYWORDS.put("while", "while");
            KEYWORDS.put("struct", "struct");
            KEYWORDS.put("read", "read");
            KEYWORDS.put("write", "write");
            KEYWORDS.put("integer", "integer");
            KEYWORDS.put("float", "float");
            KEYWORDS.put("char", "char");
            KEYWORDS.put("string", "string");
            KEYWORDS.put("bool", "bool");
        }

        public Lexer(String input) {
            this.input = input;
        }

        public List<Token> tokenize() throws Exception {
            while (pos < input.length()) {
                char current = input.charAt(pos);

                if (Character.isWhitespace(current)) {
                    if (current == '\n') line++;
                    pos++;
                    continue;
                }

                if (input.startsWith("<-", pos)) { addToken("<-", "<-"); pos += 2; continue; }
                if (input.startsWith("**", pos)) { addToken("**", "**"); pos += 2; continue; }
                if (input.startsWith("=/", pos)) { addToken("=/", "=/"); pos += 2; continue; }

                if ("{}(),;+-%*/><=!.".indexOf(current) != -1) {
                    addToken(String.valueOf(current), String.valueOf(current));
                    pos++;
                    continue;
                }

                if (Character.isLetter(current) || current == '_') {
                    StringBuilder sb = new StringBuilder();
                    while (pos < input.length() && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_' || input.charAt(pos) == '$' || input.charAt(pos) == '#')) {
                        sb.append(input.charAt(pos));
                        pos++;
                    }
                    String word = sb.toString();
                    if (KEYWORDS.containsKey(word)) {
                        addToken(KEYWORDS.get(word), word);
                    } else {
                        addToken("ID", word);
                    }
                    continue;
                }

                if (Character.isDigit(current)) {
                    StringBuilder sb = new StringBuilder();
                    while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
                        sb.append(input.charAt(pos));
                        pos++;
                    }
                    addToken("CONST", sb.toString());
                    continue;
                }

                throw new Exception("Caracter necunoscut la linia " + line + ": " + current);
            }
            return tokens;
        }

        private void addToken(String type, String value) {
            tokens.add(new Token(type, value, line));
        }
    }

    private String startSymbol = null;
    private Set<String> nonTerminals = new HashSet<>();
    private Set<String> terminals = new HashSet<>();
    private Map<String, List<List<String>>> grammar = new LinkedHashMap<>();
    private Map<String, Set<String>> firstSets = new HashMap<>();
    private Map<String, Set<String>> followSets = new HashMap<>();
    private Map<String, Map<String, List<String>>> parsingTable = new HashMap<>();
    private boolean isLL1Compatible = true;
    private PrintWriter logWriter;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== MENIU ===");
            System.out.println("1. Partea 1 (Tokeni manuali - Gramatica Didactica)");
            System.out.println("2. Partea 2 (Cod Sursa - Minilimbaj)");
            System.out.println("0. Iesire");
            System.out.print("Alegeti o optiune: ");

            String choice = scanner.nextLine();

            if (choice.equals("0")) break;

            String grammarFile = "";
            boolean useLexer = false;

            if (choice.equals("1")) {
                grammarFile = "src/gramatica1.txt";
                useLexer = false;
            } else if (choice.equals("2")) {
                grammarFile = "src/limbaj.txt";
                useLexer = true;
            } else {
                System.out.println("Optiune invalida!");
                continue;
            }

            runAnalysis(grammarFile, useLexer, scanner);
        }
    }

    private static void runAnalysis(String grammarFile, boolean useLexer, Scanner scanner) {
        LL1Parser parser = new LL1Parser();
        try {
            parser.logWriter = new PrintWriter(new FileWriter("output_analiza.txt"));
            System.out.println("\n[INFO] Incarcare gramatica: " + grammarFile);

            parser.loadGrammar(grammarFile);
            parser.computeFirst();
            parser.computeFollow();
            parser.buildParsingTable();

            if (!parser.isLL1Compatible) {
                System.out.println("[EROARE] Gramatica NU este LL(1). Vezi fisierul de log.");
                parser.logWriter.close();
                return;
            }
            System.out.println("[OK] Gramatica valida.");

            List<Token> tokens = new ArrayList<>();

            if (useLexer) {
                String sourceFile = "src/program.mlp";
                System.out.println("[INFO] Se citeste fisierul sursa: " + sourceFile);
                String sourceCode = readFile(sourceFile);

                System.out.println("[INFO] Tokenizare cod sursa...");
                Lexer lexer = new Lexer(sourceCode);
                tokens = lexer.tokenize();

                parser.logWriter.println("\nTOKENI DETECTATI DIN SURSA:");
                for(Token t : tokens) parser.logWriter.println("Linia " + t.line + ": Tip=" + t.type + " Valoare=" + t.value);

            } else {
                System.out.println("Introduceti secventa de tokeni (separati prin spatiu):");
                String inputLine = scanner.nextLine();
                String[] parts = inputLine.trim().split("\\s+");
                for (String p : parts) tokens.add(new Token(p, p, 1));
            }

            parser.parse(tokens);
            parser.logWriter.close();

        } catch (Exception e) {
            System.out.println("EROARE: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String readFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }

    public void loadGrammar(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split("->");
            if (parts.length < 2) continue;
            String lhs = parts[0].trim();
            String[] rhs = parts[1].trim().split("\\s+");
            if (startSymbol == null) startSymbol = lhs;
            nonTerminals.add(lhs);
            grammar.putIfAbsent(lhs, new ArrayList<>());
            grammar.get(lhs).add(Arrays.asList(rhs));
        }
        br.close();
        for (List<List<String>> rules : grammar.values()) {
            for (List<String> rule : rules) {
                for (String sym : rule) {
                    if (!nonTerminals.contains(sym) && !sym.equals(EPSILON)) terminals.add(sym);
                }
            }
        }
        terminals.add(END_MARKER);
    }

    public void computeFirst() {
        for (String nt : nonTerminals) firstSets.put(nt, new HashSet<>());
        for (String t : terminals) firstSets.put(t, new HashSet<>(Collections.singletonList(t)));
        firstSets.put(EPSILON, new HashSet<>(Collections.singletonList(EPSILON)));
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String lhs : grammar.keySet()) {
                for (List<String> rhs : grammar.get(lhs)) {
                    boolean allEps = true;
                    for (String sym : rhs) {
                        boolean symEps = false;
                        for (String f : firstSets.getOrDefault(sym, new HashSet<>())) {
                            if (!f.equals(EPSILON)) {
                                if (firstSets.get(lhs).add(f)) changed = true;
                            } else symEps = true;
                        }
                        if (!symEps) { allEps = false; break; }
                    }
                    if (allEps) if (firstSets.get(lhs).add(EPSILON)) changed = true;
                }
            }
        }
    }

    public void computeFollow() {
        for (String nt : nonTerminals) followSets.put(nt, new HashSet<>());
        followSets.get(startSymbol).add(END_MARKER);
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String lhs : grammar.keySet()) {
                for (List<String> rhs : grammar.get(lhs)) {
                    for (int i = 0; i < rhs.size(); i++) {
                        String B = rhs.get(i);
                        if (!nonTerminals.contains(B)) continue;
                        Set<String> betaFirst = new HashSet<>();
                        boolean betaEps = true;
                        for (int j = i + 1; j < rhs.size(); j++) {
                            String sym = rhs.get(j);
                            boolean symEps = false;
                            for (String f : firstSets.get(sym)) {
                                if (!f.equals(EPSILON)) betaFirst.add(f);
                                else symEps = true;
                            }
                            if (!symEps) { betaEps = false; break; }
                        }
                        for (String f : betaFirst) if (followSets.get(B).add(f)) changed = true;
                        if (betaEps) for (String f : followSets.get(lhs)) if (followSets.get(B).add(f)) changed = true;
                    }
                }
            }
        }
    }

    public void buildParsingTable() {
        logWriter.println("--- TABELA DE ANALIZA ---");
        for (String lhs : grammar.keySet()) {
            parsingTable.put(lhs, new HashMap<>());
            for (List<String> rhs : grammar.get(lhs)) {
                Set<String> firstRHS = new HashSet<>();
                boolean eps = true;
                for (String sym : rhs) {
                    boolean symEps = false;
                    for (String f : firstSets.get(sym)) {
                        if (!f.equals(EPSILON)) firstRHS.add(f);
                        else symEps = true;
                    }
                    if (!symEps) { eps = false; break; }
                }
                if (eps) firstRHS.add(EPSILON);
                for (String a : firstRHS) if (!a.equals(EPSILON)) addRule(lhs, a, rhs);
                if (firstRHS.contains(EPSILON)) for (String b : followSets.get(lhs)) addRule(lhs, b, rhs);
            }
        }
    }

    private void addRule(String lhs, String term, List<String> rhs) {
        if (parsingTable.get(lhs).containsKey(term)) {
            if (!parsingTable.get(lhs).get(term).equals(rhs)) {
                logWriter.println("CONFLICT " + lhs + " , " + term);
                isLL1Compatible = false;
            }
        } else {
            parsingTable.get(lhs).put(term, rhs);
            logWriter.println("M[" + lhs + "," + term + "] = " + rhs);
        }
    }

    public void parse(List<Token> input) {
        input.add(new Token(END_MARKER, "$", -1));
        Stack<String> stack = new Stack<>();
        stack.push(END_MARKER);
        stack.push(startSymbol);
        int ptr = 0;
        List<String> derivation = new ArrayList<>();

        logWriter.println("\n--- START PARSARE ---");

        while (!stack.isEmpty()) {
            String top = stack.peek();
            Token currentToken = input.get(ptr);
            String currentType = currentToken.type;

            if (top.equals(currentType)) {
                stack.pop();
                ptr++;
                if (top.equals(END_MARKER)) {
                    System.out.println("[SUCCESS] Program acceptat sintactic!");
                    logWriter.println("SUCCESS");
                    return;
                }
            } else if (terminals.contains(top)) {
                System.out.println("[FAIL] Eroare sintactica la linia " + currentToken.line +
                        ": Se astepta '" + top + "' dar s-a gasit '" + currentToken.value + "'");
                logWriter.println("EROARE LINIA " + currentToken.line);
                return;
            } else if (parsingTable.containsKey(top) && parsingTable.get(top).containsKey(currentType)) {
                stack.pop();
                List<String> prod = parsingTable.get(top).get(currentType);
                derivation.add(top + " -> " + prod);
                logWriter.println(top + " -> " + prod);
                for (int i = prod.size() - 1; i >= 0; i--) {
                    if (!prod.get(i).equals(EPSILON)) stack.push(prod.get(i));
                }
            } else {
                System.out.println("[FAIL] Eroare sintactica la linia " + currentToken.line +
                        ": Nu se poate deriva Neterminalul [" + top + "] cu intrarea '" + currentToken.value + "'");
                logWriter.println("EROARE LINIA " + currentToken.line);
                return;
            }
        }
    }
}