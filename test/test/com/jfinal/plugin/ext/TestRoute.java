package test.com.jfinal.plugin.ext;

import static org.junit.Assert.assertEquals;

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
		assertEquals("zhoulei", findAttrAfterInvoke("name"));
		assertEquals(24, findAttrAfterInvoke("age"));
	}


}
