package com.jfinal.render.chart.amchart;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import com.jfinal.render.chart.KeyLabel;

@SuppressWarnings("serial")
public class AmChartRender extends Render implements AmChartConstans {
	
	private static final String contentType = "text/html;charset=" + getEncoding();
	
	private static int GLOBAL_HEIGHT = 400;
	
	private static int GLOBAL_WIDTH = 300;
	
	/** 报表数据源*/
	private List amchartsList;
	
	private List<String> amchartsSeriesNames;
	
	private List<KeyLabel> pies;
	
    /** 报表div的高度 */
	private  int height ;
	
	/** 报表div的宽度 */
	private  int width ;
	
	/** amcharts文件存放路径 */
	private  String path = PATH ; 
	
	/** 报表flash存放路径 */
	private  String flashFile;
	
	/** 报表settings配置文件存放的路径 */
	private  String settingsFile ;
	
	/** 报表页面的标题 */
	private  String title;

    /**  报表数据字符串*/
	private  String chartXml;
	
	/**图形类型*/
	private String type;
	
	public AmChartRender(String type,int height,int width,String flashFile,String settingsFile){
		this.type = type;
		this.height = height;
		this.width = width;
		this.flashFile = flashFile;
		this.settingsFile = settingsFile;
	}
	
	public static AmChartRender pie(String flashFile,String settingsFile){
		return new AmChartRender(PIE,0,0, flashFile, settingsFile);
	}
	
	public static AmChartRender pie(int height,int width,String flashFile,String settingsFile){
		return new AmChartRender(PIE, height>0?height:GLOBAL_HEIGHT, width>0?width:GLOBAL_WIDTH, flashFile, settingsFile);
	}
	
	public static AmChartRender simple(String flashFile,String settingsFile){
		return new AmChartRender(SIMPLE,0,0, flashFile, settingsFile);
	}
	public static AmChartRender simple(int height,int width,String flashFile,String settingsFile){
		return new AmChartRender(SIMPLE,height>0?height:GLOBAL_HEIGHT, width>0?width:GLOBAL_WIDTH, flashFile, settingsFile);
	}
	
	public static AmChartRender multiple(String flashFile,String settingsFile){
		return new AmChartRender(MULTIPLE,0,0, flashFile, settingsFile);
	}
	public static AmChartRender multiple(int height,int width,String flashFile,String settingsFile){
		return new AmChartRender(MULTIPLE, height>0?height:GLOBAL_HEIGHT, width>0?width:GLOBAL_WIDTH, flashFile, settingsFile);
	}
	@Override
	public void render() {
		  genChartXml();
		    StringBuffer chartJsp = new StringBuffer();
		    chartJsp.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><title>amCharts Example</title></head><body style='background-color:#EEEEEE'>")
		    .append("<script type='text/javascript' src='").append(path).append("/flash/swfobject.js'></script><div id='chartdiv'></div><script type='text/javascript'>")
		    .append("var params = {bgcolor:'#FFFFFF'}; var flashVars = {path: '").append(path).append("/flash/',settings_file:  encodeURIComponent('").append(SETTINGS_FILE_BASE).append(settingsFile).append("'),")
		    .append("chart_data: \"").append(chartXml).append("\"};")
		    .append("swfobject.embedSWF('").append(path).append("/flash/").append(flashFile).append("', 'chartdiv', '").append(width).append("', '").append(height).append("', '8.0.0', '").append(path).append("/flash/expressInstall.swf', flashVars, params);</script></body></html>");
			PrintWriter writer = null;
			try {
				response.setHeader("Pragma", "no-cache");
		        response.setHeader("Cache-Control", "no-cache");
		        response.setDateHeader("Expires", 0);
				response.setContentType(contentType);
				writer = response.getWriter();
				writer.write(chartJsp.toString());
				writer.flush();
			} catch (IOException e) {
				throw new RenderException(e);
			} finally {
				writer.close();
			}
	}

	private void genChartXml() {
    	if(SIMPLE.equals(type)){
    		SimpleChart simpleChart = new SimpleChart();
    		simpleChart.setSeriesNames(amchartsSeriesNames);
    		simpleChart.setValues(amchartsList);
    		chartXml = Creater.createSimpleChart(simpleChart);
    	}else if(MULTIPLE.equals(type)){
    		MultipleChart multipleChart = new MultipleChart();
    		multipleChart.setSeriesNames(amchartsSeriesNames);
    		multipleChart.setValues(amchartsList);
    		chartXml = Creater.createMultipleChart(multipleChart);
    	}else if(PIE.equals(type)){ 
    		PieChart pieChart = new PieChart();
    		if(pies==null){
//    			pies = AmChartsUtil.createKeyLabelFrom2List(amchartsSeriesNames,amchartsList);
    		}
    		pieChart.setPies(pies);
    		chartXml = Creater.createPieChart(pieChart);
    	}
    	
	}


}
