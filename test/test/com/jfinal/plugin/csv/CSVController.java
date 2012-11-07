package test.com.jfinal.plugin.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import com.demo.blog.Blog;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.csv.CsvRender;

public class CSVController extends Controller{
	public void index(){
		System.out.println("--------------");
		String[] str1={"a1","b1","c1","d1"};
		String[] str2={"a2","b2","c2","d2"};
		String[] str3={"a3","b3","c3","d3"};
//		List arr1=Arrays.asList(str1); 
//		List arr2=Arrays.asList(str2);
//		List arr3=Arrays.asList(str3);
		List data=new ArrayList();  
		data.add(str1);
		data.add(str2);
		data.add(str3);
		List header=new ArrayList();
		header.add("列数1");
		header.add("列数2");
		header.add("列数3");
		List columns=new ArrayList();
		//columns.add(3);
		columns.add("id");
		columns.add("title");
		//使用blogDemo进行的测试
//		List<Blog> blogs=Blog.dao.find("select * from blog");
		List<Record> records=Db.find("select * from blog");
		render(CsvRender.instaceRender(header,records, "csvTest.csv",columns));
	}
}
