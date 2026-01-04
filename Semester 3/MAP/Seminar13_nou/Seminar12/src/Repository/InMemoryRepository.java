package Repository;

import Domain.Entity;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class InMemoryRepository<ID, E extends Entity<ID>> implements IRepository<ID, E> {
    Dictionary<ID, E> elemente;

    public InMemoryRepository() {
        this.elemente = new Hashtable<>();
    }

    public Enumeration<E> findAll() {
        return elemente.elements();
    }
}
