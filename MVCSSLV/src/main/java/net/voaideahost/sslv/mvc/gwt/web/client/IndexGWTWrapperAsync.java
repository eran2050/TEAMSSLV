package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IndexGWTWrapperAsync {

	void greet(String input, AsyncCallback<String> callback);
}
