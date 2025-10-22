public class FIPEntry {
    private final String atom;
    private final String codAtom;
    private final Integer tsIndex;

    public FIPEntry(String atom, String codAtom, Integer tsIndex) {
        this.atom = atom;
        this.codAtom = codAtom;
        this.tsIndex = tsIndex;
    }

    @Override
    public String toString() {
        return String.format("%-15s | %-10s | %s",
                atom, codAtom, tsIndex == null ? "-" : tsIndex);
    }
}
