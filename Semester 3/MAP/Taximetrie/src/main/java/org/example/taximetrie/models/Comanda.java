package org.example.taximetrie.models;

import org.example.taximetrie.repos.ClientRepository;

import java.time.LocalDateTime;

public class Comanda extends Entity<Long>{
    private Client client;
    private Sofer sofer;
    private LocalDateTime data;

    public Comanda(Client client, Sofer sofer, LocalDateTime data) {
        this.client = client;
        this.sofer = sofer;
        this.data = data;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Sofer getSofer() {
        return sofer;
    }

    public void setSofer(Sofer sofer) {
        this.sofer = sofer;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "client=" + client +
                ", sofer=" + sofer +
                ", data=" + data +
                '}';
    }

}
