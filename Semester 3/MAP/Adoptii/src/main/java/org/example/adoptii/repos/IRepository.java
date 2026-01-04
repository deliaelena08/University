package org.example.adoptii.repos;
import org.example.adoptii.models.AnimalType;
import org.example.adoptii.models.Entity;
import org.example.adoptii.models.TransferRequests;

import java.util.List;
import java.util.Optional;

public interface IRepository<ID,E extends Entity<ID>> {

    /**
     * id-the id of the entity to be find
     * return - the entity if id is valid
     *       - null if id has no entity
     * throws Exception if id is null
     */
    Optional<E> find(ID id);

    /**
     * return all entities
     */
    Iterable<E> findAll();

    /**
     * entity - must be not null otherwise throws IllegalArgumentExceptiom
     * throws InvalidEception if the entity is invalid
     * return the entity if it can be saved
     * null if already exists
     */
    Optional<E> save(E entity);
    Optional<E> update(E entity);

    default List<E> filterByType(AnimalType type) {
        throw new UnsupportedOperationException("Not implemented");
    }
    default List<TransferRequests> findByAnimalId(Integer animalId) {
        throw new UnsupportedOperationException("Not implemented");
    }
}

