package test.com.jfinal.render.excel;

import static com.jfinal.render.excel.ExcelRender.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
public class ExcelController extends Controller {
	String[] headers = new String[] { "电话号码", "设备id", "imsi", "最后上线时间" };
	String[] headers2 = new String[] { "电话号码", "设备id", "imsi"};
	String[] columns = new String[] { "ACC_NBR","DEVID","IMSI"};
	public void record() {
		List<Object> data = new ArrayList<Object>();
		for (int i = 0; i < 5; i++) {
			Record record = new Record();
			Map<String, Object> map = getMap(i);
			record.setColumns(map);
			data.add(record);
		}
		render(excel(data, headers));
	}

	public void map() {
		List<Object> data = new ArrayList<Object>();
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = getMap(i);
			data.add(map);
		}
		render(excel(data, headers));
	}
	
	public void  columns(){
		List<Object> data = new ArrayList<Object>();
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = getMap(i);
			data.add(map);
		}
		render(excel(data,headers2,columns));
		
	}
	
	private Map<String, Object> getMap(int i) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ACC_NBR", "ACC_NBR" + i);
		map.put("IMSI", "IMSI" + i);
		map.put("DEVID", "DEVID" + i);
		map.put("LASTTIME", "LASTTIME" + i);
		return map;
	}
}
