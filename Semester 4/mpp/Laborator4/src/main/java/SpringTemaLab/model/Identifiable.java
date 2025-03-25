package SpringTemaLab.model;

public interface Identifiable<Tid> {
    Tid getID();
    void setID(Tid id);
}
