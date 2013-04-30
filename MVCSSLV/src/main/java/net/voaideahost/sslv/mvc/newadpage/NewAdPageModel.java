package net.voaideahost.sslv.mvc.newadpage;

import java.util.ArrayList;

public class NewAdPageModel {

	private String						userName;
	private String						loginStatus;
	private String						name;
	private String						savedStatus;
	private String						msgOut;
	private ArrayList<NewAdPageParams>	paramList	= new ArrayList<NewAdPageParams>();
	private String						appVer;

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSavedStatus() {
		return savedStatus;
	}

	public void setSavedStatus(String savedStatus) {
		this.savedStatus = savedStatus;
	}

	public String getMsgOut() {
		return msgOut;
	}

	public void setMsgOut(String msgOut) {
		this.msgOut = msgOut;
	}

	public ArrayList<NewAdPageParams> getParamList() {
		return paramList;
	}

	public void setParamList(ArrayList<NewAdPageParams> paramList) {
		this.paramList = paramList;
	}

	public String getAppVer() {
		return appVer;
	}

	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}

}
