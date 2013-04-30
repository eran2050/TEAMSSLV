package net.voaideahost.sslv.mvc.gwt.app.server;

import net.voaideahost.sslv.dao.users.UsersDAO;
import net.voaideahost.sslv.domain.users.Users;
import net.voaideahost.sslv.mvc.gwt.app.client.IndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IndexServiceImpl implements IndexService {

	@Autowired
	private UsersDAO	uDao;

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
