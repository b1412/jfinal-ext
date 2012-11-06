package test.com.jfinal.render.excel;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.test.ControllerTestCase;
@Deprecated
public class TestExcelRender extends ControllerTestCase {
	@BeforeClass
	public static void init() throws Exception{
		start(new ExcelConfig());
	}
	
	@Test
	public void testDeleteError() throws Exception {
		invoke("/excel");
	}
}
