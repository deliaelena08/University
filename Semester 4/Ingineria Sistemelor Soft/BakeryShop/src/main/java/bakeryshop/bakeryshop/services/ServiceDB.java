package bakeryshop.bakeryshop.services;

import bakeryshop.bakeryshop.repos.IRepository;

import java.util.List;

public class ServiceDB<T> implements IService<T> {

    protected final IRepository<T> repo;

    public ServiceDB(IRepository<T> repo) {
        this.repo = repo;
    }

    @Override
    public T getById(int id) {
        return repo.getById(id);
    }

    @Override
    public List<T> getAll() {
        return repo.getAll();
    }

    @Override
    public T save(T entity) {
        return repo.save(entity);
    }

    @Override
    public void delete(int id) {
        repo.delete(id);
    }

    @Override
    public T update(T entity) {
        return repo.update(entity);
    }
}

