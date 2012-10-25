package test.com.jfinal.plugin.sqlinxml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jfinal.plugin.sqlinxml.SqlInXmlPlugin;
import com.jfinal.plugin.sqlinxml.SqlKit;

public class TestSqlinxml {

	@Test
	public void test() throws InterruptedException {
		SqlInXmlPlugin plugin = new SqlInXmlPlugin();
		plugin.start();
		assertEquals("select * from blog",SqlKit.sql("blog.findBlog"));
		assertEquals("select * from user",SqlKit.sql("blog.findUser"));
	}
	

}
