package com.jfinal.render.excel;

import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

@SuppressWarnings("serial")
public class ExcelRender extends Render {
	private static final String DEFAULT_FILE_NAME = "file1.xls";
	private static final String DEFAULT_SHEET_NAME = "sheet1";
	
	private List<Object>  data;
	private String[] columns;
	private String[] headers;
	private String fileName;
	private String sheetName;
	
	
	public ExcelRender (){}
	public ExcelRender(String fileName ,String sheetName,String[]headers,List<Object> data){
		this.fileName = fileName;
		this.headers = headers;
		this.data=data;
	}
	public ExcelRender(String fileName ,String sheetName,String[]headers,String[]columns,List<Object> data){
		this.fileName = fileName;
		this.headers = headers;
		this.columns=columns;
		this.data=data;
	}
	
	public static ExcelRender excel(List<Object>  data,String[]headers){
		return new ExcelRender(DEFAULT_FILE_NAME,DEFAULT_SHEET_NAME, headers, data);
	}
	
	public static ExcelRender excel(List<Object> data,String[]headers,String[]columns){
		ExcelRender render = new ExcelRender(DEFAULT_FILE_NAME,DEFAULT_SHEET_NAME, headers, data);
		render.setColumns(columns);
		return render;
	}
	
	public static ExcelRender excel(List<Object>  data,String fileName,String[]headers){
		return new ExcelRender(fileName, DEFAULT_SHEET_NAME,headers, data);
	}
	public static ExcelRender excel(List<Object>  data,String fileName,String sheetName,String[]headers){
		ExcelRender render = new ExcelRender(fileName, sheetName,headers, data);
		render.setSheetName(sheetName);
		return render;
	}
	public static ExcelRender excel(List<Object>  data,String fileName,String sheetName,String[]headers,String[]columns){
		ExcelRender render = new ExcelRender(fileName, sheetName,headers, data);
		render.setSheetName(sheetName);
		render.setColumns(columns);
		return render;
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
		} catch (Exception e) {
			throw new RenderException(e);
		}
	}
	public String[] getColumns() {
		return columns;
	}
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
	public String[] getHeaders() {
		return headers;
	}
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public List<Object> getData() {
		return data;
	}
	public void setData(List<Object> data) {
		this.data = data;
	}
	
	

}
