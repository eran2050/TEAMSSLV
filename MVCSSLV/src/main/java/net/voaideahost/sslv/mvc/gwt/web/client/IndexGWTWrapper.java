package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("gwtService")
public interface IndexGWTWrapper extends RemoteService {

	String getMainListing(int page);

	Integer getTotalAds();

	String getAdDesc(int adDescId);

	String doLogin(String userName, String sessionID);

	String doLogout(String userName, String sessionID);
}
