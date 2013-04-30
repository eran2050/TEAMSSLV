package mvc.gwt.application.client;

import org.springframework.stereotype.Component;

@Component
public interface GreetingService {

	String greet(String name);
}
