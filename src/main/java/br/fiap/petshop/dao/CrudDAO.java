package br.fiap.petshop.dao;

import java.util.List;

public interface CrudDAO<T> {
    void insert(T t);
    T findById(Long id);
    List<T> findAll();
    void update(T t);
    void deleteById(Long id);
}