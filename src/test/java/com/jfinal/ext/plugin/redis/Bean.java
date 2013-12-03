package com.jfinal.ext.plugin.redis;

import java.io.Serializable;


public class Bean implements Serializable{
    private static final long serialVersionUID = 7046841619418653960L;
    private int age;

    private String name;

    @Override
    public String toString() {
        return "Bean{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
