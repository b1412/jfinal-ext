package test.com.jfinal.ext;

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
	public void testAController() throws Exception {
		invoke("/aa");
		assertEquals("zhoulei", findAttrAfterInvoke("name"));
		assertEquals(24, findAttrAfterInvoke("age"));
	}
	@Test
	public void testBController() throws Exception {
		invoke("/bb");
		assertEquals("zhoulei", findAttrAfterInvoke("name"));
		assertEquals(24, findAttrAfterInvoke("age"));
	}


}
