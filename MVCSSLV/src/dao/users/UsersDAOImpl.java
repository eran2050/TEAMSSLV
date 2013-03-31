package dao.users;

import org.hibernate.Session;

import util.HibernateUtil;
import domain.loginpage.Users;

public class UsersDAOImpl implements UsersDAO {

	private Session getSession() {
		Session s = HibernateUtil.getSessionFactory().openSession();
		if (!s.isConnected())
			s.reconnect(null);

		return s;
	}

	private void close(Session s) {

		if (s != null && s.isOpen()) {
			s.close();
		}
	}

	@Override
	public Users getUserById(String s1) {
		
		Session s = getSession();
		Users u = (Users) s.get(Users.class, s1);
		close(s);

		return u;
	}
}
