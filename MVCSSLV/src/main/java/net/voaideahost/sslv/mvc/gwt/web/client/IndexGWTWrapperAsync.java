package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IndexGWTWrapperAsync {

	void getMainListing(int page, AsyncCallback<String> asyncCallback);

	void getTotalAds(AsyncCallback<Integer> asyncCallback);

	void getAdDesc(int adDescId, AsyncCallback<String> asyncCallback);

	void doLogin(String userName, String sessionID, AsyncCallback<String> asyncCallback);

	void doLogout(String userName, String sessionID, AsyncCallback<String> asyncCallback);
}
