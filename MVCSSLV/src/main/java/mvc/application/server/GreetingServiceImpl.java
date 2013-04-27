package mvc.application.server;

import java.text.MessageFormat;

import mvc.application.client.GreetingService;

import org.springframework.stereotype.Service;

@Service("greetingService")
public class GreetingServiceImpl implements GreetingService {
	@Override
	public String greetServer(String name) throws IllegalArgumentException {
		return MessageFormat.format("Hello, {0}!", name);
	}
}
