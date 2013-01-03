package test.com.jfinal.ext.plugin.tablebind;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.plugin.tablebind.ParamNameStyles;
import com.jfinal.plugin.druid.DruidPlugin;

public class TestTableBind {
	private static DruidPlugin druidPlugin;
	
	private static AutoTableBindPlugin atbp;
	@BeforeClass
	public static void init() {
		druidPlugin = new DruidPlugin("jdbc:mysql://127.0.0.1/jfinal_demo", "root", "root");
		druidPlugin.start();
	}

	@Test
	public void testDefault() {
		atbp = new AutoTableBindPlugin(druidPlugin);
		atbp.start();
	}
	
	@Test
	public void testInJar(){
		atbp = new AutoTableBindPlugin(druidPlugin);
		atbp.setAutoScan(false);
		atbp.addJar("modelInJar.jar");
		atbp.start();
	}
	
	@Test 
	public void testMoudle(){
		atbp = new AutoTableBindPlugin(druidPlugin,ParamNameStyles.lowerModule("SNS_"));
		atbp.start();
	}
	@AfterClass 
	public static void stop (){
		druidPlugin.stop();
	}
	

}
