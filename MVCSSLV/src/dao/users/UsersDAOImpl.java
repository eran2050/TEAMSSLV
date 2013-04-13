package dao.users;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dao.BaseDAO;
import domain.users.Users;

@Component
@Transactional
public class UsersDAOImpl extends BaseDAO implements UsersDAO {

    public Users getUserById(String s1) {

        Session s = getSession();
        Users u = null;
        try {
            u = (Users) s.get(Users.class, s1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }
}
