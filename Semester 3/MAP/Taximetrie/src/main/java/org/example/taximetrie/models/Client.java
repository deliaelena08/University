package org.example.taximetrie.models;

public class Client extends Persoana {

    public Client(String username, String name) {
        super(username, name);
    }

    @Override
    public String toString() {
        return "Client{" +
                "username='" + getUsername() + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }
}

