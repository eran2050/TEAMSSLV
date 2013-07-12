package test.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import net.voaideahost.sslv.dao.users.UsersDAO;
import net.voaideahost.sslv.domain.users.Users;

import org.junit.Test;
import org.mockito.Mock;

import test.MockitoBaseJava2;

public class UsersTest extends MockitoBaseJava2 {

	@Mock
	UsersDAO usersDao;

	@Test
	public void getAllUsers() {

		Users user = new Users();
		user.seteMail("vasja@com.lv");
		user.setId("1");
		user.setName("Vasja");
		user.setPhone("112");
		user.setSurName("Pupken");

		when(usersDao.getUserById("1")).thenReturn(user);

		assertEquals(user.getName(), "Vasja");
	}
}
