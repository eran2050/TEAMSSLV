package util.rest;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/serviceUsers")
public class serviceUsers {

	private Logger logger = LoggerFactory.getLogger(serviceUsers.class);

	@Autowired
	UsersDAO usersDao;	

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody
	String getUserById(@PathVariable String id) {

		Users user = usersDao.getUserById(id);
		Gson json = new Gson();
		String gson = json.toJson(user);
		logger.info("getUserById(" + id + ") : " + gson);

		return gson;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	String getAllUsers() {

		ArrayList<Users> users = usersDao.getAllUsers();
		Gson json = new Gson();
		String gson = json.toJson(users);
		logger.info("getAllUsers() : " + gson);

		return gson;
	}
}
