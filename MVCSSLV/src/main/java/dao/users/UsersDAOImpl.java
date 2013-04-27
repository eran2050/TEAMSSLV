package dao.users;

import java.util.ArrayList;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dao.BaseDAO;
import domain.users.Users;

@Component
@Transactional
public class UsersDAOImpl extends BaseDAO implements UsersDAO {

	public Users getUserById(String s1) {

		try {
			Session s = getSession();
			Users u = null;
			u = (Users) s.get(Users.class, s1);
			return u;
		} catch (Exception e) {
			// handled by aspect
		}
		return null;
	}

	@SuppressWarnings ("unchecked")
	@Override
	public ArrayList<Users> getAllUsers() {

		try {
			Session s = getSession();
			ArrayList<Users> list = null;
			list = (ArrayList<Users>) s.createCriteria(Users.class).list();
			return list;
		} catch (Exception e) {
			// handled by aspect
		}
		return null;
	}
}
