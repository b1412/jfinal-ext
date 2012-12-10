package com.jfinal.ext.render.chart.funshion;


import java.util.List;

import com.jfinal.ext.kit.KeyLabel;


public class PieChart {
	/**
	 * 标题
	 */
	private String caption;
	/**
	 * x坐标说明
	 */
	private String xAxisName;
	/**
	 * y坐标说明
	 */
	private String yAxisName;
	/**
	 * 数据源
	 */
	private List<KeyLabel> list;
	
	/**
	 * FLASH位置
	 */
	private String charUrl;
	/**
	 * FLASH宽
	 */
	private String charWidth;
	
	/**
	 * FLASH高
	 */
	private String charHigh;
	
	/**
	 * freemaker模板路径
	 */
	private String fltPath;
	
	private String mapRoot;
	

	
	public String getCharWidth() {
		return charWidth;
	}

	public void setCharWidth(String charWidth) {
		this.charWidth = charWidth;
	}

	public String getCharHigh() {
		return charHigh;
	}

	public void setCharHigh(String charHigh) {
		this.charHigh = charHigh;
	}


	public String getxAxisName() {
		return xAxisName;
	}

	public void setxAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	public String getyAxisName() {
		return yAxisName;
	}

	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}

	public String getCharUrl() {
		return charUrl;
	}

	public void setCharUrl(String charUrl) {
		this.charUrl = charUrl;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getXAxisName() {
		return xAxisName;
	}

	public void setXAxisName(String axisName) {
		xAxisName = axisName;
	}

	public String getYAxisName() {
		return yAxisName;
	}

	public void setYAxisName(String axisName) {
		yAxisName = axisName;
	}

	
	public String getFltPath() {
		return fltPath;
	}

	public void setFltPath(String fltPath) {
		this.fltPath = fltPath;
	}
	
	

	public String getMapRoot() {
		return mapRoot;
	}

	public void setMapRoot(String mapRoot) {
		this.mapRoot = mapRoot;
	}

	public List<KeyLabel> getList() {
		return list;
	}

	public void setList(List<KeyLabel> list) {
		this.list = list;
	}
  

}
