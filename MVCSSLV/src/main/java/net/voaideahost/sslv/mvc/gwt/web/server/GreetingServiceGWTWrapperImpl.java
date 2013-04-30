package net.voaideahost.sslv.mvc.gwt.web.server;

import net.voaideahost.sslv.mvc.gwt.app.client.GreetingService;
import net.voaideahost.sslv.mvc.gwt.web.client.GreetingServiceGWTWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class GreetingServiceGWTWrapperImpl extends
		AutoinjectingRemoteServiceServlet implements GreetingServiceGWTWrapper {
	private static final long	serialVersionUID	= 1L;

	private GreetingService		greetingServiceSpring;

	public String greet(String name) {

		return greetingServiceSpring.greet(name);
	}

	@Autowired
	@Required
	public void setGreetingService(GreetingService greetingService) {
		this.greetingServiceSpring = greetingService;
	}
}
