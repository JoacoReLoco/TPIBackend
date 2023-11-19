package utn.frc.sistemas.services;

import java.util.List;

@org.springframework.stereotype.Service
public interface Service <T, ID>{
    void add(T entity);
    void update(T entity);

    void delete(ID id);

    T getById(ID id);

    List<T> getAll();
}
