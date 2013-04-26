package dao.ads;

import java.util.ArrayList;

import domain.ads.Ads;

public interface AdsDAO {

	ArrayList<Ads> getMainListing(int page);

	ArrayList<Ads> getAdsListByUserAndPage(String s, int page);
	
	ArrayList<Ads> getAdsListByUser(String s);

	int getTotalAdsCount();

	int getAdsCountByUser(String s);

	Ads getSingleAdsById(int adsId);

	boolean deleteSingleAdsById(int adsId);

	void updateAds(Ads ads);

}
