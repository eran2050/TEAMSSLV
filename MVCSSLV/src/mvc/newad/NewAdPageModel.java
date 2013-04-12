package mvc.newad;

import java.util.ArrayList;

import mvc.IModel;

public class NewAdPageModel implements IModel{

	private String			userName;
	private String			loginStatus;
	private String      	name;
	private String			savedStatus;
	private String 			msgOut;
	private ArrayList<NewAdPageParams> paramList = new ArrayList<NewAdPageParams>();
	/*
	  ArrayList<Apple> apples = new ArrayList<Apple>();
	    apples.add(new GrannySmith());
	    apples.add(new Gala());
	    apples.add(new Fuji());
	    apples.add(new Braeburn());
	*/
	
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
	
}
