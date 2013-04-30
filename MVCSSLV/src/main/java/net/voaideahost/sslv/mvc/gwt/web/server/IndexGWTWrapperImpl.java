package net.voaideahost.sslv.mvc.gwt.web.server;

import net.voaideahost.sslv.mvc.gwt.app.client.IndexService;
import net.voaideahost.sslv.mvc.gwt.web.client.IndexGWTWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class IndexGWTWrapperImpl extends
		AutoinjectingRemoteServiceServlet implements IndexGWTWrapper {
	private static final long	serialVersionUID	= 1L;

	private IndexService		greetingServiceSpring;

	public String greet(String name) {

		return greetingServiceSpring.greet(name);
	}

	@Autowired
	@Required
	public void setGreetingService(IndexService greetingService) {
		this.greetingServiceSpring = greetingService;
	}
}
