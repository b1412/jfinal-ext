package com.jfinal.ext.render.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.ext.plugin.tablebind.Blog;
import com.jfinal.plugin.activerecord.Record;

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
        render(JxlsRender.me(templateFileName).filename(filename).beans(beans));
    }

    public void model() {
        Blog model = new Blog();
        model.set("title", "Derek");
        model.set("content", "35");
        Blog model2 = new Blog();
        model2.set("title", "Oleg");
        model2.set("content", "31");
        List<Blog> blogs  = Lists.newArrayList();
        blogs.add(model);
        blogs.add(model2);
        Map<String, Object> beans = Maps.newHashMap();
        beans.put("employee", blogs);
        String templateFileName = "/home/kid/git/jfinal-ext/resource/employees.xls";
        String filename = "test.xls";
        render(JxlsRender.me(templateFileName).filename(filename).beans(beans));
    }

    public void para() {
        List<Employee> staff = new ArrayList<Employee>();
        staff.add(new Employee("Derek", 35, 3000, 0.30));
        staff.add(new Employee("Elsa", 28, 1500, 0.15));
        staff.add(new Employee("Oleg", 32, 2300, 0.25));
        staff.add(new Employee("Neil", 34, 2500, 0.00));
        staff.add(new Employee("Maria", 34, 1700, 0.15));
        staff.add(new Employee("John", 35, 2800, 0.20));
        setAttr("employee", staff);
        String templateFileName = "/home/kid/git/jfinal-ext/resource/employees.xls";
        // String filename = "test.xls";
        render(JxlsRender.me(templateFileName));
    }
    

    public void record() {
        Record record = new Record();
        record.set("name", "Derek");
        record.set("age", 35);
        record.set("payment", 3000);
        record.set("bonus", 0.30);
        Record record2 = new Record();
        record2.set("name", "Oleg");
        record2.set("age", 32);
        record2.set("payment", 2300);
        record2.set("bonus", 0.25);
        List<Record> records  = new ArrayList<Record>();
        records.add(record);
        records.add(record2);
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("employee", records);
        String templateFileName = "/home/kid/git/jfinal-ext/resource/employees.xls";
        String filename = "test.xls";
        render(JxlsRender.me(templateFileName).filename(filename).beans(beans));
    }

}
