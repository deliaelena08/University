package bakeryshop.bakeryshop.services;

import java.util.List;

public interface IService<T> {
    T getById(int id);
    List<T> getAll();
    T save(T entity);
    void delete(int id);
    T update(T entity);
}
