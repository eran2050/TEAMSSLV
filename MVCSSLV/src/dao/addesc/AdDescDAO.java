package dao.addesc;

import java.util.ArrayList;

import domain.addesc.AdDesc;

public interface AdDescDAO {

	ArrayList<AdDesc> getFullAdDesc(int adsId);

	ArrayList<AdDesc> getFullAdDesc1(int adsId);

	boolean deleteByAdsId(int adsId);

	void updateAdDesc(ArrayList<AdDesc> fullDesc);
}
