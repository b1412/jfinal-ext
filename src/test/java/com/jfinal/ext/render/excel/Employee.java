package com.jfinal.ext.render.excel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Sample Employee bean to demostrate simple export features author: Leonid Vysochyn
 */
public class Employee {

    static long current = System.currentTimeMillis();
    static Random random = new Random(System.currentTimeMillis());
    public static List<Employee> generate(int num) {
        List<Employee> result = new ArrayList<Employee>();
        for (int index = 0; index < num; index++) {
            result.add(generateOne("" + index));
        }
        return result;
    }
    public static Employee generateOne(String nameSuffix) {
        return new Employee("Employee " + nameSuffix, random.nextInt(100), 1000 + random.nextDouble() * 5000, random.nextInt(100) / 100.0d,
                new Date(current - (1000000 + random.nextInt(1000000))));
    }
    private int age;
    private Date birthDate;
    private Double bonus;
    private String name;

    private Double payment;

    private Employee superior;

    public Employee(String name, int age, double payment, double bonus) {
        this.name = name;
        this.age = age;
        this.payment = new Double(payment);
        this.bonus = new Double(bonus);
    }

    public Employee(String name, int age, double payment, double bonus, Date birthDate) {
        this.name = name;
        this.age = age;
        this.payment = new Double(payment);
        this.bonus = new Double(bonus);
        this.birthDate = birthDate;
    }

    public Employee(String name, int age, Double payment, Double bonus) {
        this.name = name;
        this.age = age;
        this.payment = payment;
        this.bonus = bonus;
    }

    public int getAge() {
        return age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Double getBonus() {
        return bonus;
    }

    public String getName() {
        return name;
    }

    public Double getPayment() {
        return payment;
    }

    public Employee getSuperior() {
        return superior;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public void setSuperior(Employee superior) {
        this.superior = superior;
    }
}
