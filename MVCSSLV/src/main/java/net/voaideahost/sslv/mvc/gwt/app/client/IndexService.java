package net.voaideahost.sslv.mvc.gwt.app.client;

import org.springframework.stereotype.Component;

@Component
public interface IndexService {

	String greet(String name);

	String getMainListing(int page);

	Integer getTotalAds();
	
	String getAdDesc(int adDescId);
}
