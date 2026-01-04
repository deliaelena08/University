import java.util.Map;

public class FIPEntry {
    private final String atom;
    private final String codAtom;
    private final Integer tsIndex;

    private static final Map<String, Integer> ATOM_CODES = Map.ofEntries(
            Map.entry("ID", 0), Map.entry("CONST", 1), Map.entry("<-", 2),
            Map.entry("+", 3), Map.entry("-", 4), Map.entry("*", 5), Map.entry("/", 6),
            Map.entry("%", 7), Map.entry("**", 8), Map.entry("<", 9), Map.entry(">", 10),
            Map.entry("=", 11), Map.entry("=/", 12), Map.entry("!", 13), Map.entry("{", 14),
            Map.entry("}", 15), Map.entry("(", 16), Map.entry(")", 17), Map.entry(",", 18),
            Map.entry("main", 19), Map.entry("integer", 20), Map.entry("float", 21),
            Map.entry("char", 22), Map.entry("string", 23), Map.entry("bool", 24),
            Map.entry("read", 25), Map.entry("write", 26), Map.entry("if", 27),
            Map.entry("else", 28), Map.entry("while", 29), Map.entry("struct", 30)
    );

    public FIPEntry(String atom, String codAtom, Integer tsIndex) {
        this.atom = atom;
        this.codAtom = codAtom;
        this.tsIndex = tsIndex;
    }

    @Override
    public String toString() {
        String code;
        if (codAtom.equals("ID") || codAtom.equals("CONST")) {
            code = ATOM_CODES.get(codAtom).toString();
        } else {
            code = ATOM_CODES.getOrDefault(codAtom, -1).toString();
        }

        String index = tsIndex == null ? "-" : tsIndex.toString();

        return String.format("%-15s | %-10s | %s", atom, code, index);
    }
}