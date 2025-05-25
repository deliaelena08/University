package Repository;

import Domain.Entity;

public abstract class InFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> implements IRepository<ID, E> {
    protected String fileName;

    abstract void loadFromFile();

    public InFileRepository(String fileName) {
        super();
        this.fileName = fileName;
    }
}
