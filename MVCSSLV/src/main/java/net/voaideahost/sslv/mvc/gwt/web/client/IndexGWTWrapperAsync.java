package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IndexGWTWrapperAsync {

	void getMainListing(String viewMode, int page, String userName, AsyncCallback<String> asyncCallback);

	void getAdDesc(int adDescId, AsyncCallback<String> asyncCallback);

	void doLogin(String userName, String sessionID, AsyncCallback<String> asyncCallback);

	void doLogout(String userName, String sessionID, AsyncCallback<String> asyncCallback);
}
