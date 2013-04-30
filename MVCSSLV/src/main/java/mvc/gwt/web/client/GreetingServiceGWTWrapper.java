package mvc.gwt.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greetingService")
public interface GreetingServiceGWTWrapper extends RemoteService {

	String greet(String name);
}
