package test.com.jfinal.ext.render.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jfinal.plugin.activerecord.Record;

/**
 * 
 * @author Hongten</br>
 * 
 *         参考地址：http://hao0610.iteye.com/blog/1160678
 * 
 */
public class XlsMain {

    public static void main(String[] args) throws IOException {
        XlsMain xlsMain = new XlsMain();
        List<Record> list = xlsMain.readXls();
        for (Record record : list) {
            System.out.println(record);
        }

    }

    /**
     * 读取xls文件内容
     * 
     * @return List<XlsDto>对象
     * @throws IOException
     *             输入/输出(i/o)异常
     */
    private List<Record> readXls() throws IOException {
        InputStream is = new FileInputStream("/home/kid/git/jfinal-ext/test/languagecodename.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        Record record = null;
        List<Record> list = new ArrayList<Record>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                record = new Record();
                // 循环列Cell
                // 0学号 1姓名 2学院 3课程名 4 成绩
                // for (int cellNum = 0; cellNum <=4; cellNum++) {
                for (int cellNum = 1; cellNum <= hssfRow.getLastCellNum(); rowNum++) {
                    HSSFCell cell = hssfRow.getCell(cellNum);
                    record.set(cellNum + "", getValue(cell));
                }
                list.add(record);
            }
        }
        return list;
    }

    /**
     * 得到Excel表中的值
     * 
     * @param hssfCell
     *            Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
    @SuppressWarnings("static-access")
    private String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

}
