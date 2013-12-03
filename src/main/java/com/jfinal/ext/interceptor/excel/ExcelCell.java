package com.jfinal.ext.interceptor.excel;

public class ExcelCell {
	private String col;
	private String field;
	private String convert;
	private String validate;

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getConvert() {
		return convert;
	}

	public void setConvert(String convert) {
		this.convert = convert;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	@Override
	public String toString() {
		return "Cell [col=" + col + ", field=" + field + ", convert=" + convert + ", validate=" + validate + "]";
	}
	
	

}