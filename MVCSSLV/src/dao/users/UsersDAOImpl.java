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

		Session s = getSession();
		Users u = null;
		u = (Users) s.get(Users.class, s1);
		return u;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Users> getAllUsers() {

		Session s = getSession();
		ArrayList<Users> list = null;
		list = (ArrayList<Users>) s.createCriteria(Users.class).list();
		return list;
	}
}
