package net.voaideahost.sslv.mvc.gwt.app.client;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public interface IndexService {

	String getMainListing(int page);

	Integer getTotalAds();

	String getAdDesc(int adDescId);

	String doLogin(String userName);

	HttpSession session();
}
