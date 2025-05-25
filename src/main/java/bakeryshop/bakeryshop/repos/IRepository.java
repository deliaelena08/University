package bakeryshop.bakeryshop.repos;
import java.util.List;

public interface IRepository<T> {
    T getById(int id);
    List<T> getAll();
    T save(T entity);
    void delete(int id);
    T update(T entity);
}
