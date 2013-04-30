package net.voaideahost.sslv.dao.users;

import java.util.ArrayList;

import net.voaideahost.sslv.domain.users.Users;


public interface UsersDAO {

    Users getUserById(String s);
    
    ArrayList<Users> getAllUsers();

}
