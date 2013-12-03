package com.jfinal.ext.interceptor.excel;

import java.util.List;

import com.google.common.collect.Lists;

public class ExcelBean {
	private String name;

	private int sheetNo;

	private int start;

	private int end;

	private String rowFilter;

	private String preExcelProcessor;

	private String postExcelProcessor;
	private String preListProcessor;
	private String postListProcessor;

	private List<ExcelCell> cellList = Lists.newArrayList();

	public void addCell(ExcelCell cell) {
		cellList.add(cell);
	}

	public String getName() {
		return name;
	}

	public String getPreListProcessor() {
		return preListProcessor;
	}

	public void setPreListProcessor(String preListProcessor) {
		this.preListProcessor = preListProcessor;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSheetNo() {
		return sheetNo;
	}

	public void setSheetNo(int sheetNo) {
		this.sheetNo = sheetNo;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getRowFilter() {
		return rowFilter;
	}

	public void setRowFilter(String rowFilter) {
		this.rowFilter = rowFilter;
	}

	public List<ExcelCell> getCellList() {
		return cellList;
	}

	public void setCellList(List<ExcelCell> cellList) {
		this.cellList = cellList;
	}

	public String getPreExcelProcessor() {
		return preExcelProcessor;
	}

	public void setPreExcelProcessor(String preExcelProcessor) {
		this.preExcelProcessor = preExcelProcessor;
	}

	public String getPostExcelProcessor() {
		return postExcelProcessor;
	}

	public void setPostExcelProcessor(String postExcelProcessor) {
		this.postExcelProcessor = postExcelProcessor;
	}

	
	public String getPostListProcessor() {
		return postListProcessor;
	}

	public void setPostListProcessor(String postListProcessor) {
		this.postListProcessor = postListProcessor;
	}

}
