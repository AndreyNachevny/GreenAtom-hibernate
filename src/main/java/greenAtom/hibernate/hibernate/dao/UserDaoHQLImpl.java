package greenAtom.hibernate.hibernate.dao;

import greenAtom.hibernate.hibernate.domain.entity.User;
import greenAtom.hibernate.hibernate.domain.entity.UserRequest;
import greenAtom.hibernate.hibernate.util.HibernateUserSessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UserDaoHQLImpl implements UserDao<User> {
    @Override
    public void save(User user) {
        try(Session session = HibernateUserSessionProvider.getSessionFactory().openSession()){
           session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Long id, User user) {
        try(Session session = HibernateUserSessionProvider.getSessionFactory().openSession()){
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Long id) {
        try(Session session = HibernateUserSessionProvider.getSessionFactory().openSession()){
            session.beginTransaction();

            Query query = session.createQuery("delete from User where id = :userId");
            query.setParameter("userId", id);
            query.executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Override
    public User findById(Long id) {
        try(Session session = HibernateUserSessionProvider.getSessionFactory().openSession()){
            session.beginTransaction();

            Query query = session.createQuery("from User where id = :userId");
            query.setParameter("userId", id);
            User user = (User) query.uniqueResult();
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public List<User> findAll(UserRequest userRequest) {
        Map<String, Object> filters = userRequest.getCriteria();
        String hql = getHqlForPagination(filters);
        Query<User> res = HibernateUserSessionProvider
                .getSessionFactory()
                .openSession()
                .createQuery(hql, User.class);
        for (String key: filters.keySet()) {
            res.setParameter(key, filters.get(key));
        }
        res.setFirstResult(userRequest.getCurrentPage() * 5).setMaxResults(5);
        return res.list();
    }

    private String getHqlForPagination(Map<String,Object> filters){
        StringBuilder sb = new StringBuilder("FROM Person");
        if (!filters.isEmpty()) {
            sb.append(" WHERE ");
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                sb.append(entry.getKey()).append(String.format(" = :%s", entry.getKey()));
                sb.append(" AND ");
            }
            sb.delete(sb.length() - 4, sb.length());
        }
        return sb.toString();
    }
}
