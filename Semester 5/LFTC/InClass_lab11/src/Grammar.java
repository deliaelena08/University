import java.io.*;
import java.util.*;

public class Grammar {
    private Set<Character> nonTerminals = new HashSet<>();
    private Set<Character> terminals = new HashSet<>();
    private Character startSymbol;

    private Map<Character, List<String>> productionRules = new HashMap<>();

    public void readFromFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        boolean inRules = false;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (!inRules) {
                if (line.startsWith("Vn") || line.startsWith("Q")) {
                    nonTerminals.addAll(extractElements(line));
                } else if (line.startsWith("Vt") || line.startsWith("Σ")) {
                    terminals.addAll(extractElements(line));
                } else if ((line.startsWith("S") || line.startsWith("q0")) && line.contains("=")) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        startSymbol = parts[1].trim().charAt(0);
                    }
                } else if (line.startsWith("P") || line.startsWith("δ")) {
                    inRules = true;
                }
            } else {
                String[] parts = line.split("->");
                if (parts.length != 2) continue;

                char leftSide = parts[0].trim().charAt(0);
                String rightSide = parts[1].trim();

                productionRules.putIfAbsent(leftSide, new ArrayList<>());
                productionRules.get(leftSide).add(rightSide);
            }
        }
        br.close();
    }

    private Set<Character> extractElements(String line) {
        int start = line.indexOf("{");
        int end = line.indexOf("}");
        if (start == -1 || end == -1) return new HashSet<>();

        String content = line.substring(start + 1, end);
        String[] elements = content.split(",");
        Set<Character> set = new HashSet<>();
        for (String e : elements) {
            if (!e.trim().isEmpty()) {
                set.add(e.trim().charAt(0));
            }
        }
        return set;
    }

    public void displayRightRecursive() {
        System.out.println("\nRight Recursive Rules: ");
        boolean found = false;

        for (Map.Entry<Character, List<String>> entry : productionRules.entrySet()) {
            char lhs = entry.getKey();
            List<String> productions = entry.getValue();

            for (String rhs : productions) {
                int index = rhs.indexOf(lhs);
                if (index > 0) {
                    System.out.println(lhs + " -> " + rhs);
                    found = true;
                }
            }
        }
        //lab5-ll1
        if (!found) {
            System.out.println("No right recursive rules found.\n");
        }
    }

    public void display() {
        System.out.println("Non-terminals (Vn): " + nonTerminals);
        System.out.println("Terminals (Vt): " + terminals);
        System.out.println("Start Symbol: " + startSymbol);
        System.out.println("Production Rules:");

        for (var entry : productionRules.entrySet()) {
            char left = entry.getKey();
            for (String right : entry.getValue()) {
                System.out.println(left + " -> " + right);
            }
        }
    }

    public void readFromKeyboard() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Non-terminals (Vn):\n" + "ex: Vn={S,A,B} \n");
        String l = sc.nextLine().trim();
        nonTerminals.addAll(extractElements(l));

        System.out.println("Enter Terminals (Vt):\n" +"ex: Vt={a,b} \n:");
        l = sc.nextLine().trim();
        terminals.addAll(extractElements(l));

        System.out.println("Enter Start Symbol:\n" +  "ex: S=S \n");
        l = sc.nextLine().trim();
        String[] parts = l.split("=");
        if (parts.length == 2) {
            startSymbol = parts[1].trim().charAt(0);
        }

        System.out.println("Enter rules (empty line to stop):\n" + "ex: S->aS \n");
        while (true) {
            l = sc.nextLine().trim();
            if (l.isEmpty()) break;

            String[] ruleParts = l.split("->");
            if (ruleParts.length != 2) {
                System.out.println("Invalid format. Ignored.");
                continue;
            }

            char leftSide = ruleParts[0].trim().charAt(0);
            String rightSide = ruleParts[1].trim();

            productionRules.putIfAbsent(leftSide, new ArrayList<>());
            productionRules.get(leftSide).add(rightSide);
        }
        System.out.println("Input finished.");
    }

    public static void main(String[] args) throws IOException {
        Grammar g = new Grammar();
        Scanner sc = new Scanner(System.in);

        System.out.println("1. Read from file (grammar.txt)");
        System.out.println("2. Read from keyboard");
        System.out.print("Option: ");
        int opt = sc.nextInt(); sc.nextLine();

        if (opt == 1) {
            g.readFromFile("src/grammar.txt");
        } else {
            g.readFromKeyboard();
        }

        while(true) {
            System.out.println("\nMenu:");
            System.out.println("1. Display Grammar");
            System.out.println("2. Display Right Recursive Rules");
            System.out.println("0. Exit");
            System.out.print("Option: ");

            int cmd = sc.nextInt();

            switch(cmd) {
                case 1:
                    g.display();
                    break;
                case 2:
                    g.displayRightRecursive();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}