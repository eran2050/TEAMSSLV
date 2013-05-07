package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IndexGWTWrapperAsync {

	void greet(String input, AsyncCallback<String> asyncCallback);

	void getMainListing(int page, AsyncCallback<String> asyncCallback);

	void getTotalAds(AsyncCallback<Integer> asyncCallback);

	void getAdDesc(int adDescId, AsyncCallback<String> asyncCallback);
}
