package com.example.demo.SocialNetwork.Repos;
import com.example.demo.SocialNetwork.Helper.Entity;
import java.util.Optional;

public interface Repository<ID,E extends Entity<ID>> {

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

    /**
     * ID - the entity which must be deleted
     * returns the entity if the remove has been succesful
     *          otherwise return null
     * throws IllegalArgumentException if the ID is null
     */
    Optional<E> delete(ID id);

    /**
     * entity - must be not null
     * returns the entity if the update was successful
     *          otherwise return null
     * throws IllegalArgumentException if the entity is null
     * throws ValidationException if the entity is not valid
     */
    Optional<E> update(E entity);
}
