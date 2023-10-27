package greenAtom.hibernate.hibernate.dao;

import greenAtom.hibernate.hibernate.domain.entity.UserRequest;

import java.util.List;

public interface UserDao<T> {

    void save(T t);

    void update(Long id, T t);

    void delete(Long id);

    T findById(Long id);

    List<T> findAll(UserRequest userRequest);
}
