package dao.ads;

import java.util.ArrayList;

import domain.ads.Ads;

public interface AdsDAO {

	ArrayList<Ads> getMainListing(int page);

	ArrayList<Ads> getByUser(String s, int page);
	
	ArrayList<Ads> getByUser(String s);

	int getCount();

	int getCountByUser(String s);

	Ads getById(int adsId);

	boolean deleteById(int adsId);

	void updateAds(Ads ads);

}
