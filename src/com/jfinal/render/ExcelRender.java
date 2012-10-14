package com.jfinal.render;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jfinal.util.ExcelKit;

@SuppressWarnings("serial")
public class ExcelRender extends Render {

	private String[] columns;
	private String[] headers;
	private String fileName = "defaultFilename";
	
	private String sheetName="sheet1";
	
	private List<Map<String,Object>>  data;
	
	
	public ExcelRender(String fileName ,String[]headers,List<Map<String,Object>>  data){
		this.fileName = fileName;
		this.headers = headers;
		this.data=data;
	}
	public ExcelRender(String fileName ,String[]headers,String[]columns,List<Map<String,Object>>   data){
		this.fileName = fileName;
		this.headers = headers;
		this.columns=columns;
		this.data=data;
	}
	@Override
	public void render() {
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="+ fileName);// 设定输出文件头
		response.setContentType("application/msexcel;charset=utf-8");// 定义输出类型
		try {
			OutputStream os = response.getOutputStream();// 取得输出流
			HSSFWorkbook wb = ExcelKit.export(sheetName, 0, headers,columns,data, 0);
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
