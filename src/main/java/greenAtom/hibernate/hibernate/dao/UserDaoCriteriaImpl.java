package greenAtom.hibernate.hibernate.dao;

import greenAtom.hibernate.hibernate.domain.entity.User;
import greenAtom.hibernate.hibernate.domain.entity.UserRequest;
import greenAtom.hibernate.hibernate.util.HibernateUserSessionProvider;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UserDaoCriteriaImpl implements UserDao<User> {
    @Override
    public void save(User user) {
        try (Session session = HibernateUserSessionProvider.getSessionFactory().openSession()){

            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root);

            session.persist(user);

            session.getTransaction().commit();

        }
    }

    @Override
    public void update(Long id, User user) {
        try (Session session = HibernateUserSessionProvider.getSessionFactory().openSession()){

            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaUpdate<User> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(User.class);
            Root<User> root = criteriaUpdate.from(User.class);
            criteriaUpdate.set("name", user.getName());
            criteriaUpdate.set("age", user.getAge());
            criteriaUpdate.set("cash", user.getCash());
            criteriaUpdate.set("isWoman", user.isWoman());
            criteriaUpdate.where(criteriaBuilder.equal(root.get("id"), user.getId()));

            session.createQuery(criteriaUpdate).executeUpdate();

            session.getTransaction().commit();

        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateUserSessionProvider.getSessionFactory().openSession()){

            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaDelete<User> criteriaDelete = criteriaBuilder.createCriteriaDelete(User.class);
            Root<User> root = criteriaDelete.from(User.class);
            criteriaDelete.where(criteriaBuilder.equal(root.get("id"), id));

            session.createQuery(criteriaDelete).executeUpdate();

            session.getTransaction().commit();

        }
    }

    @Override
    public User findById(Long id) {
        try (Session session = HibernateUserSessionProvider.getSessionFactory().openSession()){

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));

            User user = session.createQuery(criteriaQuery).getSingleResult();

            session.getTransaction().commit();

            return user;
        }
    }

    @Override
    public List<User> findAll(UserRequest userRequest) {
        Map<String, Object> filters = userRequest.getCriteria();

        try (Session session = HibernateUserSessionProvider.getSessionFactory().openSession()) {

            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root);

            for (String key: filters.keySet()) {
                Predicate pr = builder.greaterThanOrEqualTo(root.get(key), filters.get(key).toString());
                criteriaQuery.where(pr);
            }

            Query<User> query = session.createQuery(criteriaQuery);

            return query.setFirstResult(userRequest.getCurrentPage()).setMaxResults(5).getResultList();
        }
    }
}
