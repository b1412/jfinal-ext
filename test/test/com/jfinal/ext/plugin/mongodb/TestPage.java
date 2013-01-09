package test.com.jfinal.ext.plugin.mongodb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.monogodb.MongodbKit;
import com.jfinal.ext.plugin.monogodb.MongodbPlugin;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


public class TestPage {
    public static void main(String[] args) throws Exception {
        MongodbPlugin mongodbPlugin = new MongodbPlugin("172.16.0.33", 27017, "test");
        mongodbPlugin.start();
        Map<String, String> filter = null;
        Map<String, String> like = null;
        Map<String, String> sort = new HashMap<>();
        sort.put("age", "desc");
        Page<Record> page = MongodbKit.paginate("test", 1, 10, filter, like, sort);
        List<Record> list = page.getList();
        int i = 0;
        for (Record record : list) {
            System.out.println(i++);
            System.out.println(record);
        }
    }
}
