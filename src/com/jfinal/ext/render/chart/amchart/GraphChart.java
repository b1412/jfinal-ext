package com.jfinal.ext.render.chart.amchart;

import java.util.List;

public class GraphChart {
	/**
	 * 每条数据的说明（x轴）
	 */
	private List<String> seriesNames;

	/**
	 * 数据描述
	 */
	private List<?>  values;

	public List<String> getSeriesNames() {
		return seriesNames;
	}

	public void setSeriesNames(List<String> seriesNames) {
		this.seriesNames = seriesNames;
	}

	public List<?> getValues() {
		return values;
	}

	public void setValues(List<?>  values) {
		this.values = values;
	}
	
	

}
