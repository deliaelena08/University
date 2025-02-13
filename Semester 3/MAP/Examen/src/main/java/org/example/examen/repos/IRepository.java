package org.example.examen.repos;

import org.example.examen.models.Entity;
import org.example.examen.models.Show;

import java.util.List;
import java.util.Optional;

public interface IRepository<ID,E extends Entity<ID>> {

    Optional<E> find(ID id);
    Iterable<E> findAll();
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    default List<E> findPaginatedAndSorted(int pageNumber, int pageSize){
        throw new UnsupportedOperationException("Pagination not supported");
    }
}
