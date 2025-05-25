package bakeryshop.bakeryshop.repos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public abstract class RepoDb<T> implements IRepository<T> {

    protected EntityManager entityManager;
    private final Class<T> type;

    public RepoDb(Class<T> type) {
        this.type = type;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bakery-pu");
        this.entityManager = emf.createEntityManager();
    }

    @Override
    public T getById(int id) {
        return entityManager.find(type, id);
    }

    @Override
    public List<T> getAll() {
        return entityManager.createQuery("FROM " + type.getSimpleName(), type).getResultList();
    }

    @Override
    public T save(T entity) {
        entityManager.getTransaction().begin();
        T saved = entityManager.merge(entity);
        entityManager.getTransaction().commit();
        return saved;
    }

    @Override
    public void delete(int id) {
        T entity = entityManager.find(type, id);
        if (entity != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public T update(T entity) {
        entityManager.getTransaction().begin();
        T updated = entityManager.merge(entity);
        entityManager.getTransaction().commit();
        return updated;
    }
}
