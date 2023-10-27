package greenAtom.hibernate.hibernate.service;

import greenAtom.hibernate.hibernate.dao.UserDao;
import greenAtom.hibernate.hibernate.domain.entity.User;
import greenAtom.hibernate.hibernate.domain.entity.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHqlService {

    private final UserDao userDao;


    @Autowired
    public UserHqlService(@Qualifier("userDaoHQLImpl") UserDao userDao) {
        this.userDao = userDao;
    }

    public void save(User user){
        userDao.save(user);
    }

    public void update(Long id, User user){
        userDao.update(id, user);
    }


    public void delete(Long id){
        userDao.delete(id);
    }

    public User findById(Long id){
        return (User) userDao.findById(id);
    }

    public List<User> findAll(UserRequest userRequest){
        return userDao.findAll(userRequest);
    }
}
