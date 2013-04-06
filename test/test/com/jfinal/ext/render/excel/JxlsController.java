package test.com.jfinal.ext.render.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.excel.JxlsRender;

public class JxlsController extends Controller {

    public void index() {
        List<Employee> staff = new ArrayList<Employee>();
        staff.add(new Employee("Derek", 35, 3000, 0.30));
        staff.add(new Employee("Elsa", 28, 1500, 0.15));
        staff.add(new Employee("Oleg", 32, 2300, 0.25));
        staff.add(new Employee("Neil", 34, 2500, 0.00));
        staff.add(new Employee("Maria", 34, 1700, 0.15));
        staff.add(new Employee("John", 35, 2800, 0.20));
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("employee", staff);
        String templateFileName = "/home/kid/git/jfinal-ext/resource/employees.xls";
        String filename = "test.xls";
        render(JxlsRender.me(filename, templateFileName, beans));
    }

}
