package dao.ads;

import java.util.ArrayList;

import mvc.newadpage.NewAdPageParams;

public interface NewAdDAO {

	boolean setNewAd(String name, String userName,
			ArrayList<NewAdPageParams> paramList);
	
}
