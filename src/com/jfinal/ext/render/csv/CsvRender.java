package com.jfinal.ext.render.csv;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

@SuppressWarnings("serial")
public class CsvRender extends Render {
	private static final String DEFAULT_FILE_NAME="default.csv";
	private static final String DEFAULT_ENCODE_TYPE="utf-8";
	
	private List headers;    //文本头
	private List data;   //数据
	private String fileName;   //文件名
	private String encodeType;   //编码
	private List clomuns;    //隐藏列
	
	public CsvRender() {}
	
	public CsvRender(List headers, List data, String fileName,String encodeType) {
		this.headers = headers;
		this.data = data;
		this.fileName = fileName;
		this.encodeType=encodeType;
	}
	//full constructor
	public CsvRender(List headers, List data, String fileName,
			String encodeType, List clomuns) {
		this.headers = headers;
		this.data = data;
		this.fileName = fileName;
		this.encodeType = encodeType;
		this.clomuns = clomuns;
	}

	//静态工厂方法得到对象
	//未包含隐藏列
	public static CsvRender instaceRender(List headers,List data){
		return new CsvRender(headers,data,DEFAULT_FILE_NAME,DEFAULT_ENCODE_TYPE);
	}
	public static CsvRender instaceRender(List headers,List data,String fileName){
		return new CsvRender(headers,data,fileName,DEFAULT_ENCODE_TYPE);
	}
	public static CsvRender instaceRender(List headers,List data,
			String fileName,String encodeType){
		return new CsvRender(headers,data,fileName,encodeType);
	}
	//包含隐藏列
	public static CsvRender instaceRender(List headers,List data,List clomuns){
		return new CsvRender(headers,data,DEFAULT_FILE_NAME,DEFAULT_ENCODE_TYPE,clomuns);
	}
	public static CsvRender csv(List headers,List data,
			String fileName,List clomuns){
		return new CsvRender(headers,data,fileName,DEFAULT_ENCODE_TYPE,clomuns);
	}
	public static CsvRender instaceRender(List headers,List data,
			String fileName,String encodeType,List clomuns){
		return new CsvRender(headers,data,fileName,encodeType,clomuns);
	}

	@Override
	public void render() {
		response.reset();  //清空
		BufferedOutputStream bos=null;
		try {
			response.setHeader("Content-disposition","attachment;filename=" +  
			        URLEncoder.encode(fileName,encodeType));
			response.setContentType("application/x-msdownload;charset="+encodeType);
			response.setCharacterEncoding(encodeType);  //设置编码
			bos=new BufferedOutputStream(response.getOutputStream());
			String csvVaule=CsvUtil.createCSV(headers,data,clomuns);  //得到csv字符串
			byte[] dataBytes=csvVaule.getBytes();
			bos.write(dataBytes,0,dataBytes.length);
			bos.flush();
		} catch (Exception e) {
			throw new RenderException(e);
		} finally{
			if(null!=bos){
				try {
					bos.flush();
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<Object> getData() {
		return data;
	}
	public void setData(List<Object> data) {
		this.data = data;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getEncodeType() {
		return encodeType;
	}
	public void setEncodeType(String encodeType) {
		this.encodeType = encodeType;
	}
	public List<Object> getHeaders() {
		return headers;
	}
	public void setHeaders(List<Object> headers) {
		this.headers = headers;
	}
	public List<Object> getClomuns() {
		return clomuns;
	}
	public void setClomuns(List<Object> clomuns) {
		this.clomuns = clomuns;
	}
	

}
