package com.jfinal.render.chart.amchart;

import java.util.List;

public class SimpleChart {
	
	/**
	 * 每条数据的说明（x轴）
	 */
	private List<String> seriesNames;

	/**
	 * 数据描述
	 */
	private List<String> values;

	/**
	 * @return the seriesNames
	 */
	public List<String> getSeriesNames() {
		return seriesNames;
	}

	/**
	 * @param seriesNames the seriesNames to set
	 */
	public void setSeriesNames(List<String> seriesNames) {
		this.seriesNames = seriesNames;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}


	
	
	

}
