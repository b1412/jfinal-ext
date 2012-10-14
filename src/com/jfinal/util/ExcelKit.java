package com.jfinal.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

/**
 * description:
 * 
 * @author liyupeng
 * 
 *         2012-8-17
 */
public class ExcelKit {
	private final static int MAX_ROWS = 65536;
	public final static int HEADER_ROW = 1;
	public final static int DEFAULT_CELL_WIDTH = 8000;
	/**
	 * 
	 * @param sheetName sheet名称
	 * @param cellWidth 设置单元格宽度
	 * @param headerRow 设置头列占的行数
	 * @param headers 头列值
	 * @param columns 列key(即 list<Map<String ,Ojbect>> 中 map的key)
	 * @param list 数据
	 * @return
	 */

	@SuppressWarnings("deprecation")
	private static HSSFWorkbook export(String sheetName, int cellWidth,
			int headerRow, String[] headers, String[] columns,
			List<Map<String, Object>> list,int columnNum,  boolean hasHeaders) {
		if (sheetName == null || sheetName.isEmpty()) {
			sheetName = "new sheet";
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet(sheetName);
			HSSFRow row = null;
			HSSFCell cell = null;
			setCellWidth(sheet, cellWidth, columnNum);
			if (hasHeaders) {
				row = sheet.createRow(0);
				if (headerRow <= 0) {
					headerRow = HEADER_ROW;
				}
				headerRow = Math.min(headerRow, MAX_ROWS);
				for (int h = 0, lenH = headers.length; h < lenH; h++) {
					Region region = new Region(0, (short) h, (short) headerRow - 1,
							(short) h);// 合并从第rowFrom行columnFrom列
					sheet.addMergedRegion(region);// 到rowTo行columnTo的区域
					// 得到所有区域
					sheet.getNumMergedRegions();
					sheet.setColumnWidth(h, cellWidth);
					cell = row.createCell(h);
					cell.setCellValue(headers[h]);
				}
			}
			
			if (list==null||list.size()==0) {
				return wb;
			}
			for (int i = 0, len = list.size(); i < len; i++) {
				row = sheet.createRow(i + headerRow);
				Map<String, Object> map = list.get(i);
				if(map==null){
					continue;
				}
				if(columns.length==0){//未设置显示列，默认全部
					Set<String> keys = map.keySet();
					int  columnIndex=0;
					for (String key : keys) {
						cell = row.createCell(columnIndex);
						cell.setCellValue(map.get(key)+"");
						columnIndex++;
					}
				}else{
					for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
						cell = row.createCell(j);
						cell.setCellValue(map.get(columns[j])+"");
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return wb;
	}
	/**
	 * 设置单元格宽度
	 * @param sheet
	 * @param cellWidth
	 * @param columnNum
	 */
	private static void setCellWidth(HSSFSheet sheet, int cellWidth, int columnNum){
		for (int i = 0; i < columnNum; i++) {
			sheet.setColumnWidth(i, cellWidth);
		}
		
	}
	/**
	 * @param String sheetName sheet名称
	 * @param int headerRow 设置头列占的行数
	 * @param String[] headers 头列值
	 * @param String[] columns 列key(即 list<Map<String ,Ojbect>> 中 map的key)
	 * @param List<Map<String, Object>> list 数据
	 * @param int cellWidth 设置单元格宽度
	 * @return
	 */
	public static HSSFWorkbook export(String sheetName,int headerRow, String[] headers, String[] columns,
			List<Map<String, Object>> list, int cellWidth) {
		boolean hasHeaders = false;
		int columnNum = 0;
		if (headers != null && headers.length >0) {
			hasHeaders = true;
			columnNum = headers.length;
		} 
		if (columns == null ) {
			columns = new String[]{};
		} 
		columnNum = Math.max(columnNum, columns.length);
		if (cellWidth <= 0) {
			cellWidth = DEFAULT_CELL_WIDTH;
		}
		return export(sheetName, cellWidth, headerRow, headers, columns, list, columnNum , hasHeaders);
		
	}
	public static void main(String[] args) {
		try {
			FileOutputStream os = new FileOutputStream(new File("test.xls"));
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < 3; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ACC_NBR", "ACC_NBR" + i);
				map.put("IMSI", "IMSI" + i);
				map.put("DEVID", "DEVID" + i);
				map.put("LASTTIME", "LASTTIME" + i);
				list.add(map);
			}
			String[] headers = new String[] { "电话号码", "设备id", "imsi", "最后上线时间" };
//			String[] columns = new String[] { "ACC_NBR", "IMSI", "DEVID",
//					"LASTTIME" };
			HSSFWorkbook wb = export("test", 0, headers, null, list, 0);
			wb.write(os);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
