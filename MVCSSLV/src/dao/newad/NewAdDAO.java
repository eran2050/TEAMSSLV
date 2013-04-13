package dao.newad;

import java.util.ArrayList;

import mvc.newad.NewAdPageParams;

public interface NewAdDAO {

	void setNewAd(String name, String userName,
			ArrayList<NewAdPageParams> paramList);
}
