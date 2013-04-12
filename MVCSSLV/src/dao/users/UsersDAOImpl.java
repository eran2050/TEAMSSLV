package dao.users;

import dao.BaseDAO;
import domain.loginpage.Users;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UsersDAOImpl extends BaseDAO implements UsersDAO {

    @Override
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
