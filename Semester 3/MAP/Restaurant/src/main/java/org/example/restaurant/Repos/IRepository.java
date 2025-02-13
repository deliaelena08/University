package org.example.restaurant.Repos;
import org.example.restaurant.models.Entity;

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

}

