package test.com.jfinal.plugin.sqlinxml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jfinal.plugin.sqlinxml.SqlInXmlPlugin;
import com.jfinal.plugin.sqlinxml.SqlManager;

public class TestSqlinxml {

	@Test
	public void test() throws InterruptedException {
		SqlInXmlPlugin plugin = new SqlInXmlPlugin();
		plugin.start();
		assertEquals("select * from blog",SqlManager.sql("blog.findBlog"));
		assertEquals("select * from user",SqlManager.sql("blog.findUser"));
	}
	

}
