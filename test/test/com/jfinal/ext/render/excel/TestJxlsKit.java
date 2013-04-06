
/**
 * Date:2013-4-4下午10:35:15
 * Copyright (c) 2010-2013, www.trafree.com  All Rights Reserved.
 */

package test.com.jfinal.ext.render.excel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * Date: 2013-4-4 下午10:35:15 <br/>
 * @author kid
 */
public class TestJxlsKit {
    public static void main(String[] args) throws ParsePropertyException, InvalidFormatException, IOException {
        List<Employee> staff = new ArrayList<Employee>();
        staff.add(new Employee("Derek", 35, 3000, 0.30));
        staff.add(new Employee("Elsa", 28, 1500, 0.15));
        staff.add(new Employee("Oleg", 32, 2300, 0.25));
        staff.add(new Employee("Neil", 34, 2500, 0.00));
        staff.add(new Employee("Maria", 34, 1700, 0.15));
        staff.add(new Employee("John", 35, 2800, 0.20));
        Map<String,Object> beans = new HashMap<String,Object>();
        beans.put("employee", staff);
        XLSTransformer transformer = new XLSTransformer();
        String templateFileName = "/home/kid/git/jfinal-ext/resource/employees.xls";
        String destFileName = "/home/kid/git/jfinal-ext/resource/employees-desc.xls";
        transformer.transformXLS(templateFileName, beans, destFileName);
    }
}

