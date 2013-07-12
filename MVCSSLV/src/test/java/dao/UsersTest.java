package dao;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import net.voaideahost.sslv.dao.users.UsersDAO;
import net.voaideahost.sslv.domain.users.Users;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UsersTest {

	@Autowired
	UsersDAO usersDao;

	@Test
	public void getAllUsers() {

		List<Users> users = new ArrayList<Users>();
		users = usersDao.getAllUsers();

		assertNotNull(users);
	}

}
