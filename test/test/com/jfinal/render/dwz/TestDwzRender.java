package test.com.jfinal.render.dwz;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.test.ControllerTestCase;
public class TestDwzRender extends ControllerTestCase {
	@BeforeClass
	public static void init() throws Exception{
		start(new DwzConfig());
	}
	
	@Test
	public void testDeleteError() throws Exception {
		invoke("/dwz/delete/1");
	}
	@Test
	public void testDeleteSuccess() throws Exception {
		invoke("/dwz/delete/2");
	}
}
