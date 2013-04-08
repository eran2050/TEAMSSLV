package dao.addesc;

import domain.addescpage.AdDesc;

import java.util.ArrayList;

public interface AdDescDAO {

    ArrayList<AdDesc> getFullAdDesc(int adsId);

    ArrayList<AdDesc> getFullAdDesc1(int adsId);

    void deleteByAdsId(int adsId);
}
