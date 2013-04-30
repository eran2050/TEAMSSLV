package net.voaideahost.sslv.services;

import java.util.ArrayList;

import net.voaideahost.sslv.dao.users.UsersDAO;
import net.voaideahost.sslv.domain.users.Users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping ("/service/users")
public class serviceUsers {

	private Logger	logger	= LoggerFactory.getLogger(this.getClass()
									.getSimpleName());

	@Autowired
	UsersDAO		usersDao;

	@RequestMapping (value = "/user/{id}", method = RequestMethod.GET)
	public @ResponseBody
	String getUserById(@PathVariable ("id") String id) {
		try {
			Users user = usersDao.getUserById(id);
			Gson json = new Gson();
			String gson = json.toJson(user);

			return gson;
		} catch (Exception e) {
			logger.error("getUserById() " + e.getMessage());
		}
		return null;
	}

	@RequestMapping (value = "/short", method = RequestMethod.GET)
	public @ResponseBody
	String getAllUsersShort() {

		try {
			ArrayList<Users> users = usersDao.getAllUsers();
			ArrayList<String> list = new ArrayList<String>();
			for (Users user : users) {
				list.add(user.getId());
			}
			Gson json = new Gson();
			String gson = json.toJson(list);

			return gson;
		} catch (Exception e) {
			logger.error("getAllUsersShort() " + e.getMessage());
		}
		return null;
	}

	@RequestMapping (value = "/long", method = RequestMethod.GET)
	public @ResponseBody
	String getAllUsersLong() {

		try {
			ArrayList<Users> users = usersDao.getAllUsers();
			Gson json = new Gson();
			String gson = json.toJson(users);

			return gson;
		} catch (Exception e) {
			logger.error("getAllUsersLong() " + e.getMessage());
		}
		return null;
	}
}
