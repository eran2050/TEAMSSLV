package dao.users;

import java.util.ArrayList;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dao.BaseDAO;
import domain.users.Users;

@Component
@Transactional
public class UsersDAOImpl extends BaseDAO implements UsersDAO {

	private Logger logger = LoggerFactory.getLogger(UsersDAO.class);

	public Users getUserById(String s1) {

		Session s = getSession();
		Users u = null;
		try {
			u = (Users) s.get(Users.class, s1);
			return u;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Users> getAllUsers() {
		Session s = getSession();
		ArrayList<Users> list = null;
		try {
			list = (ArrayList<Users>) s.createCriteria(Users.class).list();
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
