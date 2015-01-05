package com.jfinal.ext.interceptor.excel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.ext.interceptor.excel.convert.CellConvert;
import com.jfinal.ext.interceptor.excel.filter.RowFilter;
import com.jfinal.ext.interceptor.excel.validate.CellValidate;
import com.jfinal.ext.kit.Reflect;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExcelTools {
    private static final Logger LOG = Logger.getLogger(ExcelTools.class);

    /**
     * 读取excel中所有的sheet的数据集
     *
     * @param file start 数据开始行 end :可以手动控制数据结束行，0 <end < 默认sheet行数 ;
     *             负数表示数据取到倒数第几行：比如-1 表示数据取到 默认sheet行数-1
     * @return map的key为sheet编号 value 为结果集
     */

    @SuppressWarnings("deprecation")
    public static Map<Integer, List<List<String>>> readExcel(File file, Rule rule) {
        int start = rule.getStart();
        int end = rule.getEnd();
        Map<Integer, List<List<String>>> result = Maps.newHashMap();
        DecimalFormat format = new DecimalFormat();
        format.setGroupingUsed(false); // 秒杀科学计数
        Workbook wb;
        try {
            wb = WorkbookFactory.create(file);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.EMPTY_MAP;
        }
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet hs = wb.getSheetAt(i);
            List<List<String>> sheetList = Lists.newArrayList();
            int rows = hs.getPhysicalNumberOfRows();
            if (end >= rows) {
                end = rows;
            } else if (end <= 0) {
                end = rows + end;
            }
            for (int rowIndex = start; rowIndex < end; rowIndex++) { // all the rows of a sheet
                Row row = hs.getRow(rowIndex);
                List<String> columns = Lists.newArrayList();
                int blankCells = 0;
                int cellNum = row.getPhysicalNumberOfCells();
                for (int cellIndex = 0; cellIndex < cellNum; cellIndex++) { // all cells of a rows
                    Cell cell = row.getCell(cellIndex);
//                    if (cell == null) {
//                        columns.add(" ");
//                        continue;
//                    }
                    int cellType;
                    cellType = cell.getCellType();
                    String column = "";
                    switch (cellType) { // deal with the number format type
                        case Cell.CELL_TYPE_NUMERIC:
                            String result2 = String.valueOf(cell.getNumericCellValue());
                            if (DateUtil.isCellDateFormatted(cell)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                column = sdf.format(cell.getDateCellValue());
                            } else if (result2.toString().indexOf(".") != -1 && result2.toString().indexOf("E") != -1) {
                                column = format.format(cell.getNumericCellValue());
                            } else {
                                column = format.format(cell.getNumericCellValue());
                            }
                            break;
                        case Cell.CELL_TYPE_STRING: // deal with the normal string and date type
                            column = cell.getStringCellValue().trim();
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            column = " ";
//                            blankCells++;
                    }
                    columns.add(column.trim());
                }
//                if (blankCells >= row.getPhysicalNumberOfCells()) {
//                    break;
//                }
                List<Boolean> rowFilterFlagList = Lists.newArrayList();
                List<RowFilter> rowFilterList = Lists.newArrayList();
                for (int k = 0; k < rowFilterList.size(); k++) {
                    RowFilter rowFilter = rowFilterList.get(k);
                    rowFilterFlagList.add(rowFilter.doFilter(rowIndex, columns));
                }
                if (!rowFilterFlagList.contains(false)) {
                    sheetList.add(columns);
                }
            }
            result.put(i, sheetList);
        }
        return result;
    }

    public static List<List<String>> readSheet(File file, Rule Rule) {
        return readExcel(file, Rule).get(0);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<Model<?>> processSheet(File file, Rule Rule, Class clazz) {
        List<List<String>> srcList = readSheet(file, Rule);
        LOG.debug("excel data {} " + srcList);
        List<Model<?>> results = Lists.newArrayList();
        for (int i = 0; i < srcList.size(); i++) {
            List<String> list = srcList.get(i);
            Model<?> model = fillModel(clazz, list, Rule);
            results.add(model);
        }
        return results;
    }


    public static Model<?> fillModel(Class<?> clazz, List<String> list, Rule rule) {
        Model<?> model = Reflect.on(clazz).create().get();
        String[] values = list.toArray(new String[]{});
        String message = "";
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            Rule.Cell cell = matchCell(rule, i);
            String name = cell.getAttribute();
            String validateClassName = cell.getValidate();
            boolean valid = true;
            if (StrKit.notBlank(validateClassName)) {
                CellValidate cellValidate = Reflect.on(validateClassName).create().get();
                valid = cellValidate.validate(value);
                if (!valid) {
                    message = message + "value(" + value + ") is invalid in column " + cell.getIndex() + "</br>";
                }
            }
            if (valid) {
                Object convertedValue = value;
                String convertClassName = cell.getConvert();
                if (StrKit.notBlank(convertClassName)) {
                    CellConvert cellConvert = Reflect.on(convertClassName).get();
                    convertedValue = cellConvert.convert(value, model);
                }
                model.set(name, convertedValue);
            }
        }
        if (StrKit.notBlank(message)) {
            throw new ExcelException(message);
        }
        return model;
    }

    public static Rule.Cell matchCell(Rule rule, int index) {
        List<Rule.Cell> cells = rule.getCells();
        for (int i = 0; i < cells.size(); i++) {
            Rule.Cell cell = cells.get(i);
            if (index + 1 == cell.getIndex()) return cell;
        }
        return null;
    }

}
