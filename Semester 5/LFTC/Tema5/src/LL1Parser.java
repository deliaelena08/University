import java.io.*;
import java.util.*;

public class LL1Parser {

    public static final String EPSILON = "EPSILON";
    public static final String END_MARKER = "$";

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
            System.out.println("\n MENIU");
            System.out.println("1. Partea 1");
            System.out.println("2. Partea 2");
            System.out.println("0. Iesire");
            System.out.print("Alegeti o optiune: ");

            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("La revedere!");
                break;
            }

            String filename = "";
            if (choice.equals("1")) {
                filename = "src/gramatica1.txt";
            } else if (choice.equals("2")) {
                filename = "src/limbaj.txt";
            } else {
                System.out.println("Optiune invalida!");
                continue;
            }

            runAnalysis(filename, scanner);
        }
    }

    private static void runAnalysis(String filename, Scanner scanner) {
        LL1Parser parser = new LL1Parser();

        try {
            parser.logWriter = new PrintWriter(new FileWriter("output_analiza.txt"));

            System.out.println("\n[INFO] Se analizeaza: " + filename);
            System.out.println("[INFO] Detaliile tehnice (Tabela, Derivare) se scriu in 'output_analiza.txt'");

            parser.logWriter.println("=== RAPORT ANALIZA PENTRU: " + filename + " ===\n");

            parser.loadGrammar(filename);
            parser.computeFirst();
            parser.computeFollow();
            parser.buildParsingTable();

            if (!parser.isLL1Compatible) {
                System.out.println("[EROARE] Gramatica NU este LL(1). Verificati 'output_analiza.txt' pentru conflicte.");
                parser.logWriter.close();
                return;
            }

            System.out.println("[OK] Gramatica este compatibila LL(1).");
            System.out.println("Introduceti secventa de tokeni (separati prin spatiu):");

            String inputLine = scanner.nextLine();
            if (inputLine.trim().isEmpty()) {
                System.out.println("Input invalid!");
                parser.logWriter.close();
                return;
            }

            List<String> inputTokens = new ArrayList<>(Arrays.asList(inputLine.trim().split("\\s+")));
            parser.parse(inputTokens);

            parser.logWriter.close(); // Inchidem fisierul la final

        } catch (FileNotFoundException e) {
            System.out.println("EROARE: Fisierul '" + filename + "' nu a fost gasit!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- LOGICA DE INCARCARE ---
    public void loadGrammar(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split("->");
            if (parts.length < 2) continue;

            String lhs = parts[0].trim();
            String[] rhsSymbols = parts[1].trim().split("\\s+");

            if (startSymbol == null) startSymbol = lhs;
            nonTerminals.add(lhs);
            grammar.putIfAbsent(lhs, new ArrayList<>());
            grammar.get(lhs).add(Arrays.asList(rhsSymbols));
        }
        br.close();

        for (List<List<String>> rules : grammar.values()) {
            for (List<String> rule : rules) {
                for (String sym : rule) {
                    if (!nonTerminals.contains(sym) && !sym.equals(EPSILON)) {
                        terminals.add(sym);
                    }
                }
            }
        }
        terminals.add(END_MARKER);
    }

    // --- ALGORITMI ---
    public void computeFirst() {
        for (String nt : nonTerminals) firstSets.put(nt, new HashSet<>());
        for (String t : terminals) firstSets.put(t, new HashSet<>(Collections.singletonList(t)));
        firstSets.put(EPSILON, new HashSet<>(Collections.singletonList(EPSILON)));

        boolean changed = true;
        while (changed) {
            changed = false;
            for (String lhs : grammar.keySet()) {
                for (List<String> rhs : grammar.get(lhs)) {
                    boolean allDeriveEpsilon = true;
                    for (String sym : rhs) {
                        boolean symDerivesEpsilon = false;
                        Set<String> symFirst = firstSets.getOrDefault(sym, new HashSet<>());
                        for (String f : symFirst) {
                            if (!f.equals(EPSILON)) {
                                if (firstSets.get(lhs).add(f)) changed = true;
                            } else {
                                symDerivesEpsilon = true;
                            }
                        }
                        if (!symDerivesEpsilon) {
                            allDeriveEpsilon = false;
                            break;
                        }
                    }
                    if (allDeriveEpsilon) {
                        if (firstSets.get(lhs).add(EPSILON)) changed = true;
                    }
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

                        Set<String> firstBeta = new HashSet<>();
                        boolean betaDerivesEpsilon = true;
                        for (int j = i + 1; j < rhs.size(); j++) {
                            String sym = rhs.get(j);
                            Set<String> symFirst = firstSets.get(sym);
                            boolean symHasEpsilon = false;
                            for (String f : symFirst) {
                                if (!f.equals(EPSILON)) firstBeta.add(f);
                                else symHasEpsilon = true;
                            }
                            if (!symHasEpsilon) {
                                betaDerivesEpsilon = false;
                                break;
                            }
                        }

                        for (String f : firstBeta) {
                            if (followSets.get(B).add(f)) changed = true;
                        }
                        if (betaDerivesEpsilon) {
                            for (String f : followSets.get(lhs)) {
                                if (followSets.get(B).add(f)) changed = true;
                            }
                        }
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
                boolean derivesEpsilon = true;
                for (String sym : rhs) {
                    Set<String> symFirst = firstSets.get(sym);
                    boolean symHasEpsilon = false;
                    for (String f : symFirst) {
                        if (!f.equals(EPSILON)) firstRHS.add(f);
                        else symHasEpsilon = true;
                    }
                    if (!symHasEpsilon) {
                        derivesEpsilon = false;
                        break;
                    }
                }
                if (derivesEpsilon) firstRHS.add(EPSILON);

                for (String a : firstRHS) {
                    if (!a.equals(EPSILON)) addRuleToTable(lhs, a, rhs);
                }
                if (firstRHS.contains(EPSILON)) {
                    for (String b : followSets.get(lhs)) addRuleToTable(lhs, b, rhs);
                }
            }
        }
    }

    private void addRuleToTable(String lhs, String term, List<String> rhs) {
        if (parsingTable.get(lhs).containsKey(term)) {
            List<String> existing = parsingTable.get(lhs).get(term);
            if (!existing.equals(rhs)) {
                String msg = "CONFLICT LL(1) la M[" + lhs + ", " + term + "]: Existent " + existing + " vs Nou " + rhs;
                System.out.println(msg);
                logWriter.println(msg);
                isLL1Compatible = false;
            }
        } else {
            parsingTable.get(lhs).put(term, rhs);
            logWriter.println("M[" + lhs + ", " + term + "] = " + rhs);
        }
    }

    // --- PARSARE ---
    public void parse(List<String> input) {
        input.add(END_MARKER);
        Stack<String> stack = new Stack<>();
        stack.push(END_MARKER);
        stack.push(startSymbol);

        int ptr = 0;
        List<String> derivation = new ArrayList<>();

        logWriter.println("\n--- START PARSARE ---");
        logWriter.println("Input tokenizat: " + input);

        while (!stack.isEmpty()) {
            String top = stack.peek();
            String currentInput = input.get(ptr);

            if (top.equals(currentInput)) {
                stack.pop();
                ptr++;
                if (top.equals(END_MARKER)) {
                    System.out.println("[SUCCESS] Secventa acceptata!");
                    logWriter.println("\nSUCCESS. Sirul productiilor: ");
                    for(String s : derivation) logWriter.println(s);
                    return;
                }
            } else if (terminals.contains(top)) {
                System.out.println("[FAIL] Eroare sintactica (vezi fisier).");
                logWriter.println("EROARE: Se astepta terminalul '" + top + "' dar s-a gasit '" + currentInput + "'");
                return;
            } else if (parsingTable.containsKey(top) && parsingTable.get(top).containsKey(currentInput)) {
                stack.pop();
                List<String> production = parsingTable.get(top).get(currentInput);
                String ruleStr = top + " -> " + (production.isEmpty() ? EPSILON : String.join(" ", production));
                derivation.add(ruleStr);
                logWriter.println("Aplicam: " + ruleStr);

                for (int i = production.size() - 1; i >= 0; i--) {
                    if (!production.get(i).equals(EPSILON)) stack.push(production.get(i));
                }
            } else {
                System.out.println("[FAIL] Eroare sintactica (vezi fisier).");
                logWriter.println("EROARE: Nu exista tranzitie in tabela pentru Neterminal [" + top + "] si intrare [" + currentInput + "]");
                return;
            }
        }
    }
}