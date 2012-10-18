package test.com.jfinal.plugin.ext;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.test.ControllerTestCase;

public class TestRoute extends ControllerTestCase {
	@BeforeClass
	public static void init() throws Exception{
		start(new Config());
	}
	@Test
	public void test() throws Exception {
		invoke("/a");
	}


}
