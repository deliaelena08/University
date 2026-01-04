package ppd;

import java.io.*;
import java.util.*;

public class AutomatFinit {
    private Set<String> states = new HashSet<>();
    private Set<String> alphabet = new HashSet<>();
    private String startState;
    private Set<String> finalStates = new HashSet<>();
    private Map<String, Map<String, String>> transitions = new HashMap<>();

    public void readFromFile(String fileName) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new FileNotFoundException("Fișierul " + fileName + " nu a fost găsit în resources!");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            parseInput(br);
        }
    }

    public void readFromKeyboard() throws IOException {
        System.out.println("Introduceți automatul finit (terminați cu o linie goală):");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        parseInput(br);
    }

    private void parseInput(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
            line = line.trim();
            if (line.startsWith("states:")) {
                states.addAll(Arrays.asList(line.substring(7).trim().split(",")));
            } else if (line.startsWith("alphabet:")) {
                alphabet.addAll(Arrays.asList(line.substring(9).trim().split(",")));
            } else if (line.startsWith("start:")) {
                startState = line.substring(6).trim();
            } else if (line.startsWith("finals:")) {
                finalStates.addAll(Arrays.asList(line.substring(7).trim().split(",")));
            } else if (line.startsWith("transitions:")) {
                StringBuilder sb = new StringBuilder(line.substring(12).trim());
                // Citim toate liniile rămase pentru lista de tranziții
                String next;
                while ((next = br.readLine()) != null && !next.trim().isEmpty()) {
                    sb.append(next.trim());
                }
                parseTransitions(sb.toString());
                break;
            }
        }
    }

    private void parseTransitions(String text) {
        String[] parts = text.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;

            // Format general: (q0-0,1,2,3->q8)
            part = part.replace("(", "").replace(")", "");
            String[] p1 = part.split(">");
            if (p1.length != 2) continue;
            String to = p1[1].trim();
            String[] p2 = p1[0].split("<");
            if (p2.length != 2) continue;
            String from = p2[0].trim();
            String symbolList = p2[1].trim();

            // simbolurile pot fi separate prin virgule
            String[] symbols = symbolList.split(",");

            transitions.putIfAbsent(from, new HashMap<>());
            for (String sym : symbols) {
                sym = sym.trim();
                if (!sym.isEmpty()) {
                    transitions.get(from).put(sym, to);
                }
            }
        }
    }


    // -------------------- Afișare -----------------------
    public void display() {
        System.out.println("Stări: " + states);
        System.out.println("Alfabet: " + alphabet);
        System.out.println("Stare de start: " + startState);
        System.out.println("Stări finale: " + finalStates);
        System.out.println("Tranziții:");
        for (var entry : transitions.entrySet()) {
            for (var t : entry.getValue().entrySet()) {
                System.out.println("  (" + entry.getKey() + " < " + t.getKey() + " > " + t.getValue() + ")");
            }
        }
    }

    // -------------------- Verificare secvență -----------------------
    public boolean accept(String sequence) {
        String current = startState;
        for (char c : sequence.toCharArray()) {
            String sym = String.valueOf(c);
            if (!alphabet.contains(sym)) return false;
            Map<String, String> trans = transitions.get(current);
            if (trans == null || !trans.containsKey(sym)) return false;
            current = trans.get(sym);
        }
        return finalStates.contains(current);
    }

    // -------------------- Prefix maxim acceptat -----------------------
    public String longestAcceptedPrefix(String sequence) {
        String current = startState;
        StringBuilder prefix = new StringBuilder();
        String longest = "";

        for (char c : sequence.toCharArray()) {
            String sym = String.valueOf(c);
            if (!alphabet.contains(sym)) break;
            Map<String, String> trans = transitions.get(current);
            if (trans == null || !trans.containsKey(sym)) break;
            current = trans.get(sym);
            prefix.append(c);
            if (finalStates.contains(current)) {
                longest = prefix.toString();
            }
        }
        return longest;
    }

    // -------------------- Meniu -----------------------
    public static void main(String[] args) throws IOException {
        AutomatFinit af = new AutomatFinit();
        Scanner sc = new Scanner(System.in);

        System.out.println("1. Citire din tastatură");
        System.out.println("2. Citire din fișier");
        int opt = Integer.parseInt(sc.nextLine());

        if (opt == 1) {
            af.readFromKeyboard();
        } else {
            System.out.print("Numele fișierului: ");
            String fileName = sc.nextLine();
            af.readFromFile(fileName);
        }

        while (true) {
            System.out.println("\n--- MENIU ---");
            System.out.println("1. Afișare automat");
            System.out.println("2. Verifică secvență");
            System.out.println("3. Cel mai lung prefix acceptat");
            System.out.println("0. Ieșire");
            System.out.print("Alege: ");
            int c = Integer.parseInt(sc.nextLine());

            switch (c) {
                case 1 -> af.display();
                case 2 -> {
                    System.out.print("Introdu secvența: ");
                    String seq = sc.nextLine();
                    System.out.println("Rezultat: " + (af.accept(seq) ? "ACCEPTAT" : "RESPINS"));
                }
                case 3 -> {
                    System.out.print("Introdu secvența: ");
                    String seq = sc.nextLine();
                    String prefix = af.longestAcceptedPrefix(seq);
                    System.out.println("Cel mai lung prefix acceptat: " + (prefix.isEmpty() ? "(niciunul)" : prefix));
                }
                case 0 -> {
                    System.out.println("La revedere!");
                    return;
                }
                default -> System.out.println("Opțiune invalidă!");
            }
        }
    }
}

