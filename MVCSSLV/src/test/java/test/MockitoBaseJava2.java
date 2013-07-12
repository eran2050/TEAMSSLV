package test;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class MockitoBaseJava2 {

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

}
