package GroepswerkJava.DigitalPlanner.service;

import java.util.List;

public interface IService<T> {
    List<T> getAll();

    void save(T t);

    T getById(long id);

    //void delete(T t);

}
