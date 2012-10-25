package test.com.jfinal.render.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.excel.ExcelRender;

public class ExcelController extends Controller {
	public void index() {
		List<Object> data = new ArrayList<Object>();
		for (int i = 0; i < 5; i++) {
			Record record = new Record();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ACC_NBR", "ACC_NBR" + i);
			map.put("IMSI", "IMSI" + i);
			map.put("DEVID", "DEVID" + i);
			map.put("LASTTIME", "LASTTIME" + i);
			record.setColumns(map);
			data.add(record);
		}
		String[] headers = new String[] { "电话号码", "设备id", "imsi", "最后上线时间" };
		render(ExcelRender.excel(data, headers));
	}
}
