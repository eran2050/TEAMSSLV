package mvc.mainpage;

import java.util.ArrayList;
import java.util.List;

import domain.ads.Ads;

public class MainPageModel {

	private String			userName;
	private String			loginStatus;
	private ArrayList<Ads>	listing;
	private int				listingSize;
	private boolean			available;
	private List<String>	pageNumbers;
	private int				currentPage;
	private String			appVersion;
	private long			loadingTime;

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

	public ArrayList<Ads> getListing() {
		return listing;
	}

	public void setListing(ArrayList<Ads> listing) {
		this.listing = listing;
	}

	public int getListingSize() {
		return listingSize;
	}

	public void setListingSize(int listingSize) {
		this.listingSize = listingSize;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public List<String> getPageNumbers() {
		return pageNumbers;
	}

	public void setPageNumbers(List<String> pageNumbers) {
		this.pageNumbers = pageNumbers;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public long getLoadingTime() {
		return loadingTime;
	}

	public void setLoadingTime(long loadingTime) {
		this.loadingTime = loadingTime;
	}
}
