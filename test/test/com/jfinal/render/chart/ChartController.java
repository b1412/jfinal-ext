package test.com.jfinal.render.chart;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.render.chart.KeyLabel;
import com.jfinal.render.chart.amchart.AmChartsRender;

public class ChartController extends Controller {
	public void index(){
		List<KeyLabel> pies = new ArrayList<KeyLabel>();
		KeyLabel e= new KeyLabel("java","111");
		pies.add(e);
		KeyLabel e2= new KeyLabel("c","11");
		pies.add(e2);
		render(AmChartsRender.pie(pies, "ampie.swf", "pie_settings.xml",500,500));
	}
	
	public void multiple(){
		List<String> data = new ArrayList<String>();
		data.add("10");
		data.add("11");
		data.add("12");
		data.add("13");
		data.add("14");
		List<String> data1 = new ArrayList<String>();
		data1.add("20");
		data1.add("21");
		data1.add("22");
		data1.add("23");
		data1.add("24");
		List<List<String>> list = new ArrayList<List<String>>();
		list.add(data);
		list.add(data1);
		List<String> series = new ArrayList<String>();
		series.add("1月");
		series.add("2月");
		series.add("3月");
		series.add("4月");
		series.add("5月");
		render(AmChartsRender.graph(list, series, "amline.swf", "line_settings.xml"));
	}
	public void simple(){
		List<String> data = new ArrayList<String>();
		data.add("10");
		data.add("11");
		data.add("12");
		data.add("13");
		data.add("14");
		List<String> series = new ArrayList<String>();
		series.add("1月");
		series.add("2月");
		series.add("3月");
		series.add("4月");
		series.add("5月");
		render(AmChartsRender.graph(data, series, "amline.swf", "line_settings.xml"));
	}
}
