package util.rest;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import dao.users.UsersDAO;
import domain.users.Users;

@Controller
@RequestMapping ("/service/users")
public class serviceUsers {

	@Autowired
<<<<<<< HEAD
	UsersDAO usersDao;	
=======
	UsersDAO	usersDao;
>>>>>>> origin/Spring-MVC-Sashko-26apr2013

	@RequestMapping (value = "/user/{id}", method = RequestMethod.GET)
	public @ResponseBody
	String getUserById(@PathVariable ("id") String id) {

		try {
			Users user = usersDao.getUserById(id);
			Gson json = new Gson();
			String gson = json.toJson(user);

			return gson;
		} catch (Exception e) {
			// handled by aspect
		}
		return null;
	}
<<<<<<< HEAD
	
	@RequestMapping(method = RequestMethod.GET)
=======

	@RequestMapping (value = "/short", method = RequestMethod.GET)
>>>>>>> origin/Spring-MVC-Sashko-26apr2013
	public @ResponseBody
	String getAllUsersShort() {

<<<<<<< HEAD
		ArrayList<Users> users = usersDao.getAllUsers();
		Gson json = new Gson();
		String gson = json.toJson(users);
		logger.info("getAllUsers() : " + gson);
=======
		try {
			ArrayList<Users> users = usersDao.getAllUsers();
			ArrayList<String> list = new ArrayList<String>();
			for (Users user : users) {
				String fixed = new String(user.getId().replace("\u0026", "&"));
				list.add(fixed);
			}
			Gson json = new Gson();
			String gson = json.toJson(list);

			return gson;
		} catch (Exception e) {
			// handled by aspect
		}
		return null;
	}
>>>>>>> origin/Spring-MVC-Sashko-26apr2013

	@RequestMapping (value = "/long", method = RequestMethod.GET)
	public @ResponseBody
	String getAllUsersLong() {

		try {
			ArrayList<Users> users = usersDao.getAllUsers();
			Gson json = new Gson();
			String gson = json.toJson(users);

			return gson;
		} catch (Exception e) {
			// handled by aspect
		}
		return null;
	}
}
