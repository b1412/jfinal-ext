package com.jfinal.ext.interceptor.syslog;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;
import java.util.Set;

public class DefaultLogProccesor implements LogProcessor {
    @Override
    public void process(SysLog sysLog) {
        Map map = null;
        try {
            map = BeanUtils.describe(sysLog);
            map.remove("class");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Record record = new Record();
        record.setColumns(map);
        System.out.println(record);
//        Db.save("syslog", record);
    }

    @Override
    public String getUsername(Controller c) {
        return c.getSessionAttr("username");
    }

    @Override
    public String formatMessage(String title, Map<String, String> message) {
        String result = title;
        if (message.isEmpty()) {
            return result;
        }
        result += ", ";
        Set<Map.Entry<String, String>> entrySet = message.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue();
            result += key + ":" + value;
        }
        return result;
    }

}