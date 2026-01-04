package org.example.taximetrie.models;

public class Sofer extends Persoana {
    private String indicativMasina;
    private Long persoanaId;

    public Sofer(String username, String name, String indicativMasina, Long persoanaId) {
        super(username, name);
        this.indicativMasina = indicativMasina;
        this.persoanaId = persoanaId;
    }

    public String getIndicativMasina(){
        return indicativMasina;
    }

    public void setIndicativMasina(String indicativMasina){
        this.indicativMasina=indicativMasina;
    }

    public Long getPersoanaId() {
        return persoanaId;
    }

    public void setPersoanaId(Long persoanaId) {
        this.persoanaId = persoanaId;
    }
}

