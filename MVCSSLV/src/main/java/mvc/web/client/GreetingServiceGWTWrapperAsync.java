package mvc.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GreetingServiceGWTWrapperAsync {

	void greet(String input, AsyncCallback<String> callback);
}
