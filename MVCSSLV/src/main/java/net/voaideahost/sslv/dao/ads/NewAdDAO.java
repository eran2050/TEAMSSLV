package net.voaideahost.sslv.dao.ads;

import java.util.ArrayList;

import net.voaideahost.sslv.mvc.newadpage.NewAdPageParams;


public interface NewAdDAO {

	boolean setNewAd(String name, String userName,
			ArrayList<NewAdPageParams> paramList);
	
}
