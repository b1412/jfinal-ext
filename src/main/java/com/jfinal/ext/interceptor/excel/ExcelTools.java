package com.jfinal.ext.interceptor.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ExcelTools {
    private final static Logger logger = LoggerFactory.getLogger(ExcelTools.class);

    /**
     * 读取excel中所有的sheet的数据集
     * 
     * @param file
     *            start 数据开始行 end :可以手动控制数据结束行，0 <end < 默认sheet行数 ; 负数表示数据取到倒数第几行：比如-1 表示数据取到 默认sheet行数-1
     * @return map的key为sheet编号 value 为结果集
     */

    @SuppressWarnings("deprecation")
    public static Map<Integer, List<List<Object>>> readExcel(File file, ExcelBean excelBean) {
        int start = excelBean.getStart() - 1;
        int end = excelBean.getEnd();
        List<RowFilter> rowFilterList = null;
        Map<Integer, List<List<Object>>> result = new HashMap<Integer, List<List<Object>>>();
        DecimalFormat format = new DecimalFormat();
        format.setGroupingUsed(false); // 秒杀科学计数
        FileInputStream fis = null;
        HSSFWorkbook hwb = null;
        try {
            fis = new FileInputStream(file);
            hwb = new HSSFWorkbook(fis);
        } catch (Exception e) {
            logger.error("create excel error", e);
            return Maps.newHashMap();
        }
        for (int i = 0; i < hwb.getNumberOfSheets(); i++) {
            HSSFSheet hs = hwb.getSheetAt(i);
            List<List<Object>> sheetList = new ArrayList<List<Object>>();
            int rows = hs.getPhysicalNumberOfRows();
            if (end >= rows) {
                end = rows;
            } else if (end <= 0) {
                end = rows + end;
            }
            for (int rowNum = start; rowNum < end; rowNum++) { // all the rows of a sheet
                HSSFRow hr = hs.getRow(rowNum);
                List<Object> sr = new ArrayList<Object>();
                int blankCells = 0;
                int celllength = hr.getPhysicalNumberOfCells();
                for (short k = 0; k < celllength; k++) { // all cells of a rows
                    HSSFCell hc = hr.getCell(k);

                    int cellType = 20;
                    try {
                        cellType = hc.getCellType();
                    } catch (NullPointerException e) {
                        sr.add(" ");
                        celllength++;
                        continue;
                    }
                    String tempStr = "";
                    switch (cellType) { // deal with the number format type
                        case Cell.CELL_TYPE_NUMERIC:
                            String result2 = String.valueOf(hc.getNumericCellValue());
                            if (HSSFDateUtil.isCellDateFormatted(hc)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                tempStr = sdf.format(hc.getDateCellValue());
                            } else if (result2.toString().indexOf(".") != -1 && result2.toString().indexOf("E") != -1) {
                                tempStr = format.format(hc.getNumericCellValue());
                            } else {
                                tempStr = format.format(hc.getNumericCellValue());
                            }
                            break;
                        case Cell.CELL_TYPE_STRING: // deal with the normal string and date type
                            tempStr = hc.getStringCellValue().trim();
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            tempStr = " ";
                            blankCells++;
                    }
                    sr.add(StringUtils.deleteWhitespace(tempStr));
                }
                if (blankCells >= hr.getPhysicalNumberOfCells()) {
                    break;
                }
                List<Boolean> rowFilterFlagList = Lists.newArrayList();
                if (rowFilterList != null) {
                    for (int k = 0; k < CollectionUtils.size(rowFilterList); k++) {
                        RowFilter rowFilter = rowFilterList.get(k);
                        rowFilterFlagList.add(rowFilter.doFilter(rowNum, sr));
                    }
                }
                if (!rowFilterFlagList.contains(false)) {
                    sheetList.add(sr);
                }
            }
            result.put(i, sheetList);
        }
        try {
            fis.close();
        } catch (IOException e) {
        }
        return result;
    }

    public static List<List<Object>> readSheet(File file, ExcelBean excelBean) {
        return readExcel(file, excelBean).get(0);
    }

    /**
     * 
     * @param file
     * @param sheetNo
     * @param rulePath
     * @param clazz
     * @return
     */
    public static <T> T digesterSheetWithTwoDimensionalRule(File file, ExcelBean excelBean, Class clazz) {
        Map<String, String> ruleMap = Maps.newHashMap();
        List<ExcelCell> cellList = excelBean.getCellList();
        for (ExcelCell cell : cellList) {
            ruleMap.put(cell.getCol(), cell.getField());
        }
        Object destObj = null;
        if (clazz.equals(Map.class)) {
            destObj = Maps.newHashMap();
        } else {
            try {
                destObj = clazz.newInstance();
            } catch (Exception e) {
                destObj = null;
            }
        }
        fillObjectWithTwoDimensionalRule(destObj, readSheet(file, excelBean), ruleMap);
        return (T) destObj;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<T> digesterSheetWithOneDimensionalRule(File file, ExcelBean excelBean, Class clazz)
            throws ExcelException {
        List<List<Object>> srcList = readSheet(file, excelBean);
        logger.debug("excel data {} ", srcList);
        List<T> destList = Lists.newArrayList();
        if (srcList == null) {
            return destList;
        }
        for (int i = 0; i < srcList.size(); i++) {
            List<Object> list = srcList.get(i);
            T destObj = null;
            if (clazz.equals(Map.class)) {
                destObj = (T) Maps.newHashMap();
            } else {
                try {
                    destObj = (T) clazz.newInstance();
                } catch (Exception e) {
                    destObj = null;
                }
            }
            fillObjectWithOneDimensionalRule(destObj, list, excelBean);
            destList.add(destObj);
        }
        return destList;
    }

    /**
     * 根据二维规则和数据源向目标对象(pojo或者map)的对应字段填充值
     * 
     * @param obj
     *            目标对象
     * @param list
     *            数据源 (嵌套list) // * @param ruleMap 二维规则 (key为excel结果集行列坐标，也就是数据源转换之后的二维数组的坐标,value为字段名称)
     */
    public static void fillObjectWithTwoDimensionalRule(Object obj, List<List<Object>> list, Map<String, String> ruleMap) {
        if (obj == null) {
            return;
        }
        Object[][] objs = new Object[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            List<Object> templist = list.get(i);
            objs[i] = templist.toArray();
        }
        Set<String> keys = ruleMap.keySet();
        for (String key : keys) {
            String[] xy = StringUtils.split(key, ",");
            int x = Integer.parseInt(xy[0]) - 1;
            int y = Integer.parseInt(xy[1]) - 1;
            String name = ruleMap.get(key);
            setProperty(obj, name, objs[x][y]);
        }
    }

    /**
     * 根据一维规则和数据源向目标对象(pojo或者map)的对应字段填充值
     * 
     * @param obj
     *            目标对象
     * @param list
     *            数据源
     * @param ruleMap
     *            一维规则 (key为该行的列坐标,value为字段名称)
     * @throws ExcelException
     */
    public static void fillObjectWithOneDimensionalRule(Object obj, List<Object> list, ExcelBean excelBean)
            throws ExcelException {
        if (obj == null || list == null) {
            return;
        }
        List<ExcelCell> cellList = excelBean.getCellList();
        Object[] srcObjs = list.toArray();
        String message = "";
        for (int i = 0; i < srcObjs.length; i++) {
            Object value = srcObjs[i];
            List<ExcelCell> excelCellList = Lists.newArrayList();
            for (ExcelCell excelCell : cellList) {
                String col = excelCell.getCol();
                if (col.equals((i + 1) + "")) {
                    excelCellList.add(excelCell);
                }
            }
            if (excelCellList.isEmpty()) {
                continue;
            }
            ExcelCell excelCell = excelCellList.get(0);
            String name = excelCell.getField();
            String validateClassName = excelCell.getValidate();
            boolean flag = true;
            if (!StringUtils.isBlank(validateClassName)) {
                CellValidate cellValidate = null;
                try {
                    cellValidate = (CellValidate) Class.forName(validateClassName).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                flag = cellValidate.validate(value);
                if (!flag) {
                    message = message + excelCell.getCol() + "列  导入了非法值:" + value + "</br>";
                }
            }
            if (flag) {
                String convertClassName = excelCell.getConvert();
                if (!StringUtils.isBlank(convertClassName)) {
                    try {
                        CellConvert cellConvert = (CellConvert) Class.forName(convertClassName).newInstance();
                        value = cellConvert.convert(value, obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                setProperty(obj, name, value);
            }

        }
        if (StringUtils.isNotBlank(message)) {
            throw new ExcelException(message);
        }
    }

    /**
     * 读取规则配置文件
     * 
     * @param rulePath
     * @return
     */
    public static Map<String, String> readRule(String rulePath) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(rulePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = Maps.newLinkedHashMap();
        for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            map.put(key, properties.getProperty(key));
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static void setProperty(Object obj, String ognlExpr, Object value) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            map.put(ognlExpr, value);
        } else {
            Class clazz = obj.getClass();
            try {
                Class typeClass = getFieldTypeOfClassByOgnl(ognlExpr, clazz);
                if (typeClass.equals(Date.class)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = sdf.format(value);
                }

                value = ConvertUtils.convert(value, typeClass);
                if (typeClass.equals(Double.class)) {
                    try {
                        value = convertDouble(value);
                    } catch (NumberFormatException e) {
                        value = "0.0";
                    }
                }
//                BeanUtil.ognlPropertyValue(obj, ognlExpr, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

    }

    public static Object convertDouble(Object src) {
        Double total = 0d;
        String[] srcs = StringUtils.split(src.toString(), ",");
        for (int i = srcs.length - 1; i >= 0; i--) {
            String s = srcs[i];
            double d = Double.parseDouble(s) * Math.pow(10, 3 * (srcs.length - 1 - i));
            total = total + d;
        }
        DecimalFormat format = new DecimalFormat();
        format.setGroupingUsed(false);
        return format.format(total);
    }

    /**
     * 根据对象导航语言(不支持集合)获取一个类中字段的类型
     * 
     * @param ognlExpr
     * @param clazz
     * @return
     * @throws Exception
     */
    private static Class getFieldTypeOfClassByOgnl(String ognlExpr, Class clazz) {
        ognlExpr = StringUtils.substringBefore(ognlExpr, "(");
        String[] fields = StringUtils.split(ognlExpr, ".");
        Field field = null;
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i];
            try {
//                field = BeanUtils3.getDeclaredField(clazz, fieldName);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            if (i == fields.length - 1) {
                break;
            }
            try {
                clazz = Class.forName(clazz.getPackage().getName() + "." + StringUtils.capitalize(field.getName()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return field.getType();
    }

    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (byte element : b) {
                    int k = element;
                    if (k < 0) {
                        k += 256;
                    }
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
}
