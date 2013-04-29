package mvc.application.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.users.UsersDAO;
import domain.users.Users;

import mvc.application.client.GreetingService;

@Service
public class GreetingServiceImpl implements GreetingService {

	@Autowired
	private UsersDAO uDao;

	@Override
	public String greet(String name) {

		try {
			Users usr = uDao.getUserById(name);
			return usr.getName() + " " + usr.getSurName();
		} catch (Exception e) {
			// to do
		}

		return "Hello, " + name;
	}
}
