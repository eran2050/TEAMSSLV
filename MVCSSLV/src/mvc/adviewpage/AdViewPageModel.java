package mvc.adviewpage;

import java.util.ArrayList;

import mvc.IModel;
import domain.addesc.AdDesc;
import domain.ads.Ads;

public class AdViewPageModel implements IModel {

    private Ads ads;
    private ArrayList<AdDesc> fullDesc;
    private String userName;
    private String loginStatus;
    private boolean available;
    private int adsId;
    private String appVersion;
    private String action;
    private String form;

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

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}
}
