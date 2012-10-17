package test.com.jfinal.plugin.ext;

import org.junit.Test;

import com.jfinal.test.ControllerTestCase;

public class TestRoute extends ControllerTestCase {
	@Test
	public void test() throws Exception {
		invoke("/a");
	}


}
