package Domain;

import java.util.Objects;

public class Angajat extends Entity<String> {
    private String nume;
    private Double venitPeOra;
    private Nivel nivel;

    public Angajat(String id, String nume, Double venitPeOra, Nivel nivel) {
        super(id);
        this.nume = nume;
        this.venitPeOra = venitPeOra;
        this.nivel = nivel;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Double getVenitPeOra() {
        return venitPeOra;
    }

    public void setVenitPeOra(Double venitPeOra) {
        this.venitPeOra = venitPeOra;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    @Override
    public String toString() {
        return "Angajat{" +
                "id=" + id +
                ", nivel=" + nivel +
                ", venitPeOra=" + venitPeOra +
                ", nume='" + nume + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Angajat angajat = (Angajat) o;
        return this.getId().equals(angajat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
