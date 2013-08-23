package com.jfinal.ext.render.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample Department bean to demostrate main excel export features author: Leonid Vysochyn
 */
public class Department {
    public static List<Department> generate(int depCount, int employeeCount) {
        List<Department> departments = new ArrayList<Department>();
        for (int index = 0; index < depCount; index++) {
            Department dep = new Department("Dep " + index);
            dep.setChief(Employee.generateOne("ch" + index));
            dep.setStaff(Employee.generate(employeeCount));
            departments.add(dep);
        }
        return departments;
    }
    private Employee chief;
    private String name;

    private List<Employee> staff = new ArrayList<Employee>();

    public Department(String name) {
        this.name = name;
    }

    public Department(String name, Employee chief, List<Employee> staff) {
        this.name = name;
        this.chief = chief;
        this.staff = staff;
    }

    public void addEmployee(Employee employee) {
        staff.add(employee);
    }

    public Employee getChief() {
        return chief;
    }

    public String getName() {
        return name;
    }

    public List<Employee> getStaff() {
        return staff;
    }

    public void setChief(Employee chief) {
        this.chief = chief;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStaff(List<Employee> staff) {
        this.staff = staff;
    }
}
