package dao.addesc;

import java.util.ArrayList;

import domain.addesc.AdDesc;

public interface AdDescDAO {

	ArrayList<AdDesc> getFullAdDescByHQL(int adsId);

	ArrayList<AdDesc> getFullAdDescByCriteria(int adsId);

	boolean deleteByAdsId(int adsId);

	boolean updateAdDesc(ArrayList<AdDesc> fullDesc);
}
