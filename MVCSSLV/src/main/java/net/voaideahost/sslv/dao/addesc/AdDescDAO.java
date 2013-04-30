package net.voaideahost.sslv.dao.addesc;

import java.util.ArrayList;

import net.voaideahost.sslv.domain.addesc.AdDesc;


public interface AdDescDAO {

	ArrayList<AdDesc> getFullAdDescByHQL(int adsId);

	ArrayList<AdDesc> getFullAdDescByCriteria(int adsId);

	boolean deleteByAdsId(int adsId);

	boolean updateAdDesc(ArrayList<AdDesc> fullDesc);
}
