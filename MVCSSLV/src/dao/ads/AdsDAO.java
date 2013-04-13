package dao.ads;

import domain.mainpage.Ads;

import java.util.ArrayList;

public interface AdsDAO {

    ArrayList<Ads> getMainListing(int page);

    ArrayList<Ads> getByUser(String s, int page);

    int getCount();

    int getCountByUser(String s);

    Ads getById(int adsId);

    void deleteById(int adsId);
    
    void updateAds(Ads ads);

}
