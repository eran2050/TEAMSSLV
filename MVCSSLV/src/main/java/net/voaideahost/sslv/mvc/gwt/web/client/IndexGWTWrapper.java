package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("gwtService")
public interface IndexGWTWrapper extends RemoteService {

	String greet(String name);

	String getMainListing(int page);

	Integer getTotalAds();
}
