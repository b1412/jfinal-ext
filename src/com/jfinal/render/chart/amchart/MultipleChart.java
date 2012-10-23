package com.jfinal.render.chart.amchart;

import java.util.List;

public class MultipleChart {
	/**
	 * 每条数据的说明（x轴）
	 */
	private List<String> seriesNames;

	/**
	 * 数据描述
	 */
	private List<List<String>> values;

	public List<String> getSeriesNames() {
		return seriesNames;
	}

	public void setSeriesNames(List<String> seriesNames) {
		this.seriesNames = seriesNames;
	}

	public List<List<String>> getValues() {
		return values;
	}

	public void setValues(List<List<String>> values) {
		this.values = values;
	}
	
	

}
