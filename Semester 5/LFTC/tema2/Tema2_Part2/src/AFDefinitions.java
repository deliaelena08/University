import java.util.*;

public class AFDefinitions {

    private static final Set<String> getDigits() {
        return Set.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    }

    private static final Set<String> getLetters() {
        Set<String> letters = new HashSet<>();
        for (char c = 'a'; c <= 'z'; c++) letters.add(String.valueOf(c));
        for (char c = 'A'; c <= 'Z'; c++) letters.add(String.valueOf(c));
        return letters;
    }

    private static final Set<String> getHexDigits() {
        Set<String> hex = new HashSet<>(getDigits());
        hex.addAll(Set.of("a", "b", "c", "d", "e", "f", "A", "B", "C", "D", "E", "F"));
        return hex;
    }

    public static AFStructure getAFIdentifier() {
        Set<String> stari = Set.of("q0", "q1");
        Set<String> alfabet = new HashSet<>();
        alfabet.addAll(getLetters());
        alfabet.addAll(getDigits());
        alfabet.addAll(Set.of("_", "$", "#"));

        String stareInitiala = "q0";
        Set<String> stariFinale = Set.of("q1");

        Map<String, Map<String, String>> tranzitii = new HashMap<>();

        tranzitii.put("q0", new HashMap<>());
        for (String l : getLetters()) tranzitii.get("q0").put(l, "q1");

        tranzitii.put("q1", new HashMap<>());
        for (String a : alfabet) tranzitii.get("q1").put(a, "q1");

        return new AFStructure(stari, alfabet, stareInitiala, stariFinale, tranzitii);
    }

    public static AFStructure getAFIntegerConstant() {
        Set<String> stari = Set.of("q0", "q1", "q2", "q3", "q4", "q5", "q6", "q7", "q8", "q9", "q10", "q11", "q13");
        Set<String> alfabet = new HashSet<>();
        alfabet.addAll(Set.of("+", "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "x", "X", "b", "B", "a", "c", "d", "e", "f", "A", "C", "D", "E", "F", "U", "u", "L", "l"));
        alfabet.add("b");

        String stareInitiala = "q0";
        Set<String> stariFinale = Set.of("q1", "q3", "q6", "q7", "q8", "q9", "q10", "q11", "q13");

        Map<String, Map<String, String>> tranzitii = new HashMap<>();

        for (String s : stari) tranzitii.put(s, new HashMap<>());

        Runnable addTransitions = () -> {
            for (String s : Set.of("+", "-")) tranzitii.get("q0").put(s, "q2");
            tranzitii.get("q0").put("0", "q3");
            for (String d : Set.of("1", "2", "3", "4", "5", "6", "7", "8", "9")) tranzitii.get("q0").put(d, "q1");

            tranzitii.get("q2").put("0", "q3");
            for (String d : Set.of("1", "2", "3", "4", "5", "6", "7", "8", "9")) tranzitii.get("q2").put(d, "q1");

            for (String d : getDigits()) tranzitii.get("q1").put(d, "q1");
            for (String s : Set.of("u", "U")) tranzitii.get("q1").put(s, "q9");
            for (String s : Set.of("l", "L")) tranzitii.get("q1").put(s, "q10");

            for (String d : Set.of("0", "1", "2", "3", "4", "5", "6", "7")) tranzitii.get("q3").put(d, "q6");
            for (String s : Set.of("x", "X")) tranzitii.get("q3").put(s, "q4");
            for (String s : Set.of("b", "B")) tranzitii.get("q3").put(s, "q5");

            for (String h : getHexDigits()) tranzitii.get("q4").put(h, "q7");

            for (String b : Set.of("0", "1")) tranzitii.get("q5").put(b, "q8");

            for (String d : Set.of("0", "1", "2", "3", "4", "5", "6", "7")) tranzitii.get("q6").put(d, "q6");
            for (String s : Set.of("u", "U")) tranzitii.get("q6").put(s, "q9");
            for (String s : Set.of("l", "L")) tranzitii.get("q6").put(s, "q10");

            for (String h : getHexDigits()) tranzitii.get("q7").put(h, "q7");
            for (String s : Set.of("u", "U")) tranzitii.get("q7").put(s, "q9");
            for (String s : Set.of("l", "L")) tranzitii.get("q7").put(s, "q10");

            for (String b : Set.of("0", "1")) tranzitii.get("q8").put(b, "q8");
            for (String s : Set.of("u", "U")) tranzitii.get("q8").put(s, "q9");
            for (String s : Set.of("l", "L")) tranzitii.get("q8").put(s, "q10");

            tranzitii.get("q9").put("l", "q11");
            tranzitii.get("q9").put("L", "q11");
            tranzitii.get("q10").put("u", "q11");
            tranzitii.get("q10").put("U", "q11");
            tranzitii.get("q10").put("l", "q13");
            tranzitii.get("q10").put("L", "q13");
        };
        addTransitions.run();

        return new AFStructure(stari, alfabet, stareInitiala, stariFinale, tranzitii);
    }


    public static AFStructure getAFRealConstant() {
        Set<String> stari = Set.of("q0", "q1", "q2", "q3", "q4", "q5", "q6");
        // q0: Start
        // q1: Intreg (poate fi final daca nu e urmat de ID)
        // q2: Punct
        // q3: Zecimal (Final)
        // q4: Exponent 'e' sau 'E'
        // q5: Semn Exponent (+/-)
        // q6: Exponent Cifră (Final)

        Set<String> alfabet = new HashSet<>(getDigits());
        alfabet.addAll(Set.of(".", "e", "E", "+", "-"));

        String stareInitiala = "q0";
        Set<String> stariFinale = Set.of("q3", "q6");

        Map<String, Map<String, String>> tranzitii = new HashMap<>();
        for (String s : stari) tranzitii.put(s, new HashMap<>());

        Set<String> digits = getDigits();

        for (String d : digits) tranzitii.get("q0").put(d, "q1");

        for (String d : digits) tranzitii.get("q1").put(d, "q1");
        tranzitii.get("q1").put(".", "q2");
        for (String e : Set.of("e", "E")) tranzitii.get("q1").put(e, "q4");

        for (String d : digits) tranzitii.get("q2").put(d, "q3");

        for (String d : digits) tranzitii.get("q3").put(d, "q3");
        for (String e : Set.of("e", "E")) tranzitii.get("q3").put(e, "q4");

        for (String s : Set.of("+", "-")) tranzitii.get("q4").put(s, "q5");
        for (String d : digits) tranzitii.get("q4").put(d, "q6");
        for (String d : digits) tranzitii.get("q5").put(d, "q6");
        for (String d : digits) tranzitii.get("q6").put(d, "q6");

        return new AFStructure(stari, alfabet, stareInitiala, stariFinale, tranzitii);
    }
}