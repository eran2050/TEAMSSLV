package net.voaideahost.sslv.mvc.gwt.web.server;

import net.voaideahost.sslv.mvc.gwt.app.client.IndexService;
import net.voaideahost.sslv.mvc.gwt.web.client.IndexGWTWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class IndexGWTWrapperImpl extends AutoinjectingRemoteServiceServlet
		implements IndexGWTWrapper {
	private static final long serialVersionUID = 1L;

	private IndexService service;

	@Autowired
	@Required
	public void setIndexService(IndexService service) {
		this.service = service;
	}

	public String greet(String name) {

		return service.greet(name);
	}

	@Override
	public String getMainListing() {

		return service.getMainListing();
	}
}
