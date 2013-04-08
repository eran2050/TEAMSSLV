package mvc.adviewpage;

import domain.addescpage.AdDesc;
import domain.mainpage.Ads;
import mvc.IModel;

import java.util.ArrayList;

public class AdViewPageModel implements IModel {

    private Ads ads;
    private ArrayList<AdDesc> fullDesc;
    private String userName;
    private String loginStatus;
    private boolean available;
    private int adsId;

    public Ads getAds() {
        return ads;
    }

    public void setAds(Ads ads) {
        this.ads = ads;
    }

    public ArrayList<AdDesc> getFullDesc() {
        return fullDesc;
    }

    public void setFullDesc(ArrayList<AdDesc> fullDesc) {
        this.fullDesc = fullDesc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getAdsId() {
        return adsId;
    }

    public void setAdsId(int adsId) {
        this.adsId = adsId;
    }

}
