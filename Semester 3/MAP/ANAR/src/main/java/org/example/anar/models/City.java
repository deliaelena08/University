package org.example.anar.models;

public class City extends Entity<Integer>{
    private String name;
    private Integer river;
    private Integer cmdr;
    private Integer cma;

    public City(String name, Integer river, Integer cmdr, Integer cma) {
        this.name = name;
        this.river = river;
        this.cmdr = cmdr;
        this.cma = cma;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRiver() {
        return river;
    }

    public void setRiver(Integer river) {
        this.river = river;
    }

    public Integer getCmdr() {
        return cmdr;
    }

    public void setCmdr(Integer cmdr) {
        this.cmdr = cmdr;
    }

    public Integer getCma() {
        return cma;
    }

    public void setCma(Integer cma) {
        this.cma = cma;
    }
}
