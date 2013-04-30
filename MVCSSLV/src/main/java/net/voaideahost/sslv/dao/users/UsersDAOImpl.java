package net.voaideahost.sslv.dao.users;

import java.util.ArrayList;

import net.voaideahost.sslv.dao.BaseDAO;
import net.voaideahost.sslv.domain.users.Users;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UsersDAOImpl extends BaseDAO implements UsersDAO {

	private Logger	logger	= LoggerFactory.getLogger(this.getClass()
									.getSimpleName());

	public Users getUserById(String s1) {

		try {
			Session s = getSession();
			Users u = null;
			u = (Users) s.get(Users.class, s1);
			return u;
		} catch (Exception e) {
			logger.error("getUserById() " + e.getMessage());
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
			logger.error("getAllUsers() " + e.getMessage());
		}
		return null;
	}
}
