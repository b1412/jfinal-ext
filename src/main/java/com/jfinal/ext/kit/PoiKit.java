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
package com.jfinal.ext.kit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;
import java.util.Map.Entry;

public class PoiKit {

    public static final String VERSION_2003 = "2003";
    private static final int HEADER_ROW = 1;
    private static final int MAX_ROWS = 65535;
    private String version;
    private String sheetName = "new sheet";
    private int cellWidth = 8000;
    private int headerRow;
    private String[] headers;
    private String[] columns;
    private List<?>[] data;

    public PoiKit(List<?>... data) {
        this.data = data;
    }

    public static PoiKit data(List<?>... data) {
        return new PoiKit(data);
    }

    public static List<List<?>> dice(List<?> num, int chunkSize) {
        int size = num.size();
        int chunk_num = size / chunkSize + (size % chunkSize == 0 ? 0 : 1);
        List<List<?>> result = Lists.newArrayList();
        for (int i = 0; i < chunk_num; i++) {
            result.add(Lists.newArrayList(num.subList(i * chunkSize, i == chunk_num - 1 ? size : (i + 1) * chunkSize)));
        }
        return result;
    }

    public Workbook export() {
        Preconditions.checkNotNull(headers, "headers can not be null");
        Preconditions.checkNotNull(columns, "columns can not be null");
        Preconditions.checkArgument(cellWidth >= 0, "cellWidth < 0");
        Workbook wb;
        if (VERSION_2003.equals(version)) {
            wb = new HSSFWorkbook();
            if (data.length > 1) {
                for (List<?> item : data) {
                    Preconditions.checkArgument(item.size() < MAX_ROWS, "Invalid data size (" + item.size() + ") outside allowable range (0..65535)");
                }
            } else if (data[0].size() > MAX_ROWS) {
                data = dice(data[0], MAX_ROWS).toArray(new List<?>[]{});
            }
        } else {
            wb = new XSSFWorkbook();
        }
        if (data.length == 0) {
            return wb;
        }
        for (int i = 0; i < data.length; i++) {
            Sheet sheet = wb.createSheet(sheetName + (i == 0 ? "" : (i + 1)));
            Row row;
            Cell cell;
            if (headers.length > 0) {
                row = sheet.createRow(0);
                if (headerRow <= 0) {
                    headerRow = HEADER_ROW;
                }
                headerRow = Math.min(headerRow, MAX_ROWS);
                for (int h = 0, lenH = headers.length; h < lenH; h++) {
                    if (cellWidth > 0) {
                        sheet.setColumnWidth(h, cellWidth);
                    }
                    cell = row.createCell(h);
                    cell.setCellValue(headers[h]);
                }
            }

            for (int j = 0, len = data[i].size(); j < len; j++) {
                row = sheet.createRow(j + headerRow);
                Object obj = data[i].get(j);
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
        }
        return wb;
    }

    @SuppressWarnings("unchecked")
    private static void processAsMap(String[] columns, Row row, Object obj) {
        Cell cell;
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

    private static void processAsModel(String[] columns, Row row, Object obj) {
        Cell cell;
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

    private static void processAsRecord(String[] columns, Row row, Object obj) {
        Cell cell;
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

    public PoiKit version(String version) {
        this.version = version;
        return this;
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

    public PoiKit headers(String... headers) {
        this.headers = headers;
        return this;
    }

    public PoiKit columns(String... columns) {
        this.columns = columns;
        return this;
    }

}
