package dao.addesc;

import java.util.ArrayList;

import domain.addescpage.AdDesc;

public interface AdDescDAO {

	ArrayList<AdDesc> getFullAdDesc(int adsId);
	
	ArrayList<AdDesc> getFullAdDesc1(int adsId);
	
	void deleteByAdsId(int adsId);
}
