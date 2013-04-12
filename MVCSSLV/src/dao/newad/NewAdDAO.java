package dao.newad;

import java.util.ArrayList;

import mvc.newad.NewAdPageParams;
import dao.BaseDAO;

public interface NewAdDAO extends BaseDAO {

	void setNewAd(String name, String userName,
			ArrayList<NewAdPageParams> paramList);
	 
	//void setNewAd( String daoname, String daouser);
	
	}
