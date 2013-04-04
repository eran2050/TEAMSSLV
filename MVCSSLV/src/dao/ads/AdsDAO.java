package dao.ads;

import java.util.ArrayList;
import dao.BaseDAO;
import domain.mainpage.Ads;

public interface AdsDAO extends BaseDAO {

	ArrayList<Ads> getMainListing(int page);

	ArrayList<Ads> getByUser(String s, int page);

	int getCount();

	int getCountByUser(String s);

	Ads getById(int adsId);

}
