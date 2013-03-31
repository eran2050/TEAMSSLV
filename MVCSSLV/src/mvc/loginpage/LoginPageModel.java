package mvc.loginpage;

import java.util.ArrayList;
import java.util.List;

import mvc.IModel;
import domain.loginpage.Users;
import domain.mainpage.Ads;

public class LoginPageModel implements IModel {

	private String userName;
	private String userPassword;
	private String statusMessage;
	private String status;
	private String htmlForm;
	private ArrayList<Ads> ads;
	private Users user;
	private boolean valid;
	private List<String> pageNumbers;
	private int currentPage;
	private int listingSize;

	public String getUserName() {
		return userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<Ads> getAds() {
		return ads;
	}

	public void setAds(ArrayList<Ads> ads) {
		this.ads = ads;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getHtmlForm() {
		return htmlForm;
	}

	public void setHtmlForm(String htmlForm) {
		this.htmlForm = htmlForm;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
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

	public int getListingSize() {
		return listingSize;
	}

	public void setListingSize(int listingSize) {
		this.listingSize = listingSize;
	}

}
