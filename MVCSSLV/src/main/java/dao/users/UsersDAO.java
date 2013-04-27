package dao.users;

import java.util.ArrayList;

import domain.users.Users;

public interface UsersDAO {

    Users getUserById(String s);
    
    ArrayList<Users> getAllUsers();

}
