package Repository;

import Domain.Entity;

import java.util.Enumeration;

public interface IRepository<ID, T extends Entity<ID>> {
    Enumeration<T> findAll();
}
