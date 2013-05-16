package net.voaideahost.sslv.mvc.gwt.app.client;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public interface IndexService {

	HttpSession session();

	String getMainListing(String viewMode, int page, String userName);

	String getAdDesc(int adDescId);

	String doLogin(String userName, String sessionID);

	String doLogout(String userName, String sessionID);
}
