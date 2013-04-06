package test.com.jfinal.ext.plugin.tablebind;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class TestModel extends Model<TestModel> {
    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
