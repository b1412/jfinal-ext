package test.com.jfinal.ext.plugin.mongodb;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.ext.plugin.monogodb.MongodbKit;
import com.jfinal.ext.plugin.monogodb.MongodbPlugin;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


public class TestPage {
    public static void main(String[] args) throws Exception {
        MongodbPlugin mongodbPlugin = new MongodbPlugin("172.16.0.11", 27017, "log");
        mongodbPlugin.start();
        Map<String, String> filter = null;
        Map<String, String> like = null;
        Map<String, String> sort = new HashMap<>();
        sort.put("age", "desc");
        Page<Record> page = MongodbKit.paginate("sns", 1, 10,null,like);
        System.out.println(page.getPageNumber());
        System.out.println(page.getTotalPage());
    }
}
