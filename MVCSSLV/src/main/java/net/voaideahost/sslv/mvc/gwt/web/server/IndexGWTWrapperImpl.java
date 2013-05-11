package net.voaideahost.sslv.mvc.gwt.web.server;

import net.voaideahost.sslv.mvc.gwt.app.client.IndexService;
import net.voaideahost.sslv.mvc.gwt.web.client.IndexGWTWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class IndexGWTWrapperImpl extends AutoinjectingRemoteServiceServlet implements IndexGWTWrapper {
	private static final long serialVersionUID = 1L;

	private IndexService service;

	@Autowired
	@Required
	public void setIndexService(IndexService service) {

		this.service = service;
	}

	@Override
	public String getMainListing(String viewMode, int page, String userName) {

		return service.getMainListing(viewMode, page, userName);
	}

	@Override
	public Integer getTotalAds(String viewMode, String userName) {

		return service.getTotalAds(viewMode, userName);
	}

	@Override
	public String getAdDesc(int adDescId) {

		return service.getAdDesc(adDescId);
	}

	@Override
	public String doLogin(String userName, String sessionID) {

		return service.doLogin(userName, sessionID);
	}

	@Override
	public String doLogout(String userName, String sessionID) {

		return service.doLogout(userName, sessionID);
	}
}
