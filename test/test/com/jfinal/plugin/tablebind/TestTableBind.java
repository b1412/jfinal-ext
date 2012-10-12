package test.com.jfinal.plugin.tablebind;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.plugin.tablebind.TableNameStyle;

public class TestTableBind {
	@BeforeClass
	public static void init() {
		DruidPlugin c3p0 = new DruidPlugin(
				"jdbc:mysql://127.0.0.1/jfinal_demo", "root", "root");
		AutoTableBindPlugin atbp = new AutoTableBindPlugin(c3p0,TableNameStyle.LOWER);
		atbp.addJar("modelInJar.jar");
		c3p0.start();
		atbp.start();
	}

	@Test
	public void test01() throws InterruptedException {
	}

}
