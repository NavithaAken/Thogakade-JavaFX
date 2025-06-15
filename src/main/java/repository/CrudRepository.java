package repository;

import java.util.List;

public interface CrudRepository<T,ID> extends SuperRepository{
    boolean add(T entity);
    boolean update(T entity);
    boolean deleteById(ID id);
    T searchById(ID id);
    List<T> getAll();

}
