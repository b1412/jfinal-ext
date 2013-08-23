package com.jfinal.ext.render.excel;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

public class PoiController extends Controller {
    String[] columns = new String[] { "ACC_NBR", "DEVID", "IMSI" };
    String[] headers = new String[] { "电话号码", "设备id", "imsi", "最后上线时间" };
    String[] headers2 = new String[] { "电话号码", "设备id", "imsi" };

    public void columns() {
        List<Object> data = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = getMap(i);
            data.add(map);
        }
        render(PoiRender.me(data, headers2).columns(columns));

    }

    private Map<String, Object> getMap(int i) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("ACC_NBR", "ACC_NBR" + i);
        map.put("IMSI", "IMSI" + i);
        map.put("DEVID", "DEVID" + i);
        map.put("LASTTIME", "LASTTIME" + i);
        return map;
    }

    public void map() {
        List<Object> data = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = getMap(i);
            data.add(map);
        }
        render(PoiRender.me(data, headers));
    }

    public void record() {
        List<Object> data = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            Record record = new Record();
            Map<String, Object> map = getMap(i);
            record.setColumns(map);
            data.add(record);
        }
        render(PoiRender.me(data, headers));
    }
}
