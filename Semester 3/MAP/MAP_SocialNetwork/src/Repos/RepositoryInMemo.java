package Repos;
import Exceptions.RepoException;
import Helper.Entity;
import Exceptions.ValidationException;
import Validations.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RepositoryInMemo<ID,E extends Entity<ID>> implements Repository<ID,E> {
    private final Map<ID,E> entities;
    private final Validator<E> validator;
    public RepositoryInMemo(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }
    @Override
    public Optional<E> find(ID id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi null");
        }

        try {
            validator.validate(entity);
            if (entities.containsKey(entity.getId())) {
                throw new RepoException("Id deja existent");
            }
            entities.put(entity.getId(), entity);
            return Optional.of(entity);
        } catch (ValidationException e) {
            throw e;
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        if(id == null)
            throw new IllegalArgumentException();
        if(entities.containsKey(id)){
            E entity = entities.get(id);
            entities.remove(id);
            return Optional.of(entity);
        }
        else
            throw new RepoException("Id-ul nu exista");
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException();
        try{
            validator.validate(entity);
            if(entities.containsKey(entity.getId())){
                entities.put(entity.getId(), entity);
                return Optional.of(entity);
            }
            else
                throw new RepoException("Id-ul nu exista");
        }
        catch(ValidationException e) {
            throw e;
        }
    }
}
