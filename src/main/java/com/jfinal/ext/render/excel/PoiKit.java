/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.render.excel;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import com.google.common.base.Preconditions;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

public class PoiKit {

    private static final int HEADER_ROW = 1;
    private static final int MAX_ROWS = 65536;

    private String sheetName = "new sheet";
    private int cellWidth = 8000;
    private int headerRow;
    private String[] headers = new String[] {};
    private String[] columns;
    private List<?> data;

    public PoiKit(List<?> data) {
        this.data = data;
    }

    public static PoiKit with(List<?> data) {
        return new PoiKit(data);
    }

    public HSSFWorkbook export() {
        Preconditions.checkNotNull(headers, "headers can not be null");
        Preconditions.checkNotNull(columns, "columns can not be null");
        Preconditions.checkArgument(cellWidth >= 0, "cellWidth < 0");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFRow row = null;
        HSSFCell cell = null;
        if (headers.length > 0) {
            row = sheet.createRow(0);
            if (headerRow <= 0) {
                headerRow = HEADER_ROW;
            }
            headerRow = Math.min(headerRow, MAX_ROWS);
            for (int h = 0, lenH = headers.length; h < lenH; h++) {
                @SuppressWarnings("deprecation")
                Region region = new Region(0, (short) h, (short) headerRow - 1, (short) h);// 合并从第rowFrom行columnFrom列
                sheet.addMergedRegion(region);// 到rowTo行columnTo的区域
                // 得到所有区域
                sheet.getNumMergedRegions();
                if (cellWidth > 0) {
                    sheet.setColumnWidth(h, cellWidth);
                }
                cell = row.createCell(h);
                cell.setCellValue(headers[h]);
            }
        }
        if (data.size() == 0) {
            return wb;
        }
        for (int i = 0, len = data.size(); i < len; i++) {
            row = sheet.createRow(i + headerRow);
            Object obj = data.get(i);
            if (obj == null) {
                continue;
            }
            if (obj instanceof Map) {
                processAsMap(columns, row, obj);
            } else if (obj instanceof Model) {
                processAsModel(columns, row, obj);
            } else if (obj instanceof Record) {
                processAsRecord(columns, row, obj);
            }
        }
        return wb;
    }

    @SuppressWarnings("unchecked")
    private static void processAsMap(String[] columns, HSSFRow row, Object obj) {
        HSSFCell cell;
        Map<String, Object> map = (Map<String, Object>) obj;
        if (columns.length == 0) {// 未设置显示列，默认全部
            Set<String> keys = map.keySet();
            int columnIndex = 0;
            for (String key : keys) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(map.get(key) + "");
                columnIndex++;
            }
        } else {
            for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
                cell = row.createCell(j);
                cell.setCellValue(map.get(columns[j]) + "");
            }
        }
    }

    private static void processAsModel(String[] columns, HSSFRow row, Object obj) {
        HSSFCell cell;
        Model<?> model = (Model<?>) obj;
        Set<Entry<String, Object>> entries = model.getAttrsEntrySet();
        if (columns.length == 0) {// 未设置显示列，默认全部
            int columnIndex = 0;
            for (Entry<String, Object> entry : entries) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(entry.getValue() + "");
                columnIndex++;
            }
        } else {
            for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
                cell = row.createCell(j);
                cell.setCellValue(model.get(columns[j]) + "");
            }
        }
    }

    private static void processAsRecord(String[] columns, HSSFRow row, Object obj) {
        HSSFCell cell;
        Record record = (Record) obj;
        Map<String, Object> map = record.getColumns();
        if (columns.length == 0) {// 未设置显示列，默认全部
            record.getColumns();
            Set<String> keys = map.keySet();
            int columnIndex = 0;
            for (String key : keys) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(record.get(key) + "");
                columnIndex++;
            }
        } else {
            for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
                cell = row.createCell(j);
                cell.setCellValue(map.get(columns[j]) + "");
            }
        }
    }

    public PoiKit sheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public PoiKit cellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
        return this;
    }

    public PoiKit headerRow(int headerRow) {
        this.headerRow = headerRow;
        return this;
    }

    public PoiKit headers(String[] headers) {
        this.headers = headers;
        return this;
    }

    public PoiKit columns(String[] columns) {
        this.columns = columns;
        return this;
    }

}
