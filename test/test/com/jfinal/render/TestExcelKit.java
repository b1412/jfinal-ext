package test.com.jfinal.render;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.excel.ExcelKit;

public class TestExcelKit {
	@Test
	public void testRecord() {
		try {
			FileOutputStream os = new FileOutputStream(new File("test.xls"));
			List list = new ArrayList();
			for (int i = 0; i < 5; i++) {
				Record record = new Record();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ACC_NBR", "ACC_NBR" + i);
				map.put("IMSI", "IMSI" + i);
				map.put("DEVID", "DEVID" + i);
				map.put("LASTTIME", "LASTTIME" + i);
				record.setColumns(map);
				list.add(record);
			}
			String[] headers = new String[] { "电话号码", "设备id", "imsi", "最后上线时间" };
			String[] columns = new String[] { "ACC_NBR", "IMSI", "DEVID" };
			HSSFWorkbook wb = ExcelKit.export("test", 0, headers, columns,
					list, 0);
			wb.write(os);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testMap() {
		try {
			FileOutputStream os = new FileOutputStream(new File("test.xls"));
			List list = new ArrayList();
			for (int i = 0; i < 4; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ACC_NBR", "ACC_NBR" + i);
				map.put("IMSI", "IMSI" + i);
				map.put("DEVID", "DEVID" + i);
				map.put("LASTTIME", "LASTTIME" + i);
				list.add(map);
			}
			String[] headers = new String[] { "电话号码", "设备id", "imsi", "最后上线时间" };
			HSSFWorkbook wb = ExcelKit.export("test", 0, headers, null,list, 0);
			wb.write(os);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
