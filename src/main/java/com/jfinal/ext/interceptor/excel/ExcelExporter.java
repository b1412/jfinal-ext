package com.jfinal.ext.interceptor.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelExporter {

	public interface Cellable {
		String[] getCellValues();
	}

	public static HSSFWorkbook generateHSSFWorkbook(List<? extends Cellable> toInExcelData, String head[])throws Exception{
		if (toInExcelData == null || toInExcelData.size() == 0) {
			return null;
		}

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		//		sheet.setColumnWidth((short)1, (short)5);
		int rowIndex = 0;
		HSSFRow headRow = sheet.createRow(rowIndex++);
		for (short h = 0; h < head.length; h++) {
			HSSFCell cell = headRow.createCell(h);
			//			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(head[h]);
		}

		for (Cellable c : toInExcelData) {
			HSSFRow row = sheet.createRow(rowIndex++);

			String[] cellValue = c.getCellValues();

			for (short j = 0; j < cellValue.length; j++) {
				HSSFCell cell = row.createCell(j);
				//				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(cellValue[j]);
			}

		}
		return wb;
	}
	public static File generateExcelFile(String filePath, List<? extends Cellable> toInExcelData, String head[]) throws Exception {
		if (toInExcelData == null || toInExcelData.size() == 0) {
			return null;
		}

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		//		sheet.setColumnWidth((short)1, (short)5);
		int rowIndex = 0;
		HSSFRow headRow = sheet.createRow(rowIndex++);
		for (short h = 0; h < head.length; h++) {
			HSSFCell cell = headRow.createCell(h);
			//			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(head[h]);
		}

		for (Cellable c : toInExcelData) {
			HSSFRow row = sheet.createRow(rowIndex++);

			String[] cellValue = c.getCellValues();

			for (short j = 0; j < cellValue.length; j++) {
				HSSFCell cell = row.createCell(j);
				//				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(cellValue[j]);
			}

		}

		File file = new File(new String(filePath.getBytes(), "GBK"));
		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fileOut = new FileOutputStream(file);
		wb.write(fileOut);
		fileOut.close();
		return file;
	}
	
}
