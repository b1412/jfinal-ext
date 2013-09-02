package com.jfinal.ext.kit;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableInfo;
import com.jfinal.plugin.activerecord.TableInfoMapping;

public class ModelKit {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Record toRecord(Model model) {
        Record record = new Record();
        Set<Entry<String, Object>> attrs = model.getAttrsEntrySet();
        for (Entry<String, Object> entry : attrs) {
            record.set(entry.getKey(), entry.getValue());
        }
        return record;
    }

    @SuppressWarnings("rawtypes")
    public static Model set(Model model, Object... attrsAndValues) {
        int length = attrsAndValues.length;
        Preconditions.checkArgument(length % 2 == 0, "attrsAndValues length must be even number", length);
        for (int i = 0; i < length; i = i + 2) {
            Object attr = attrsAndValues[i];
            Preconditions.checkArgument(attr instanceof String, "the odd number of attrsAndValues  must be String");
            model.set((String) attr, attrsAndValues[i + 1]);
        }
        return model;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, Object> toMap(Model model) {
        Map<String, Object> map = Maps.newHashMap();
        Set<Entry<String, Object>> attrs = model.getAttrsEntrySet();
        for (Entry<String, Object> entry : attrs) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
    public static int[] batchSave(List<? extends Model> data) {
        return batchSave(data,data.size());
    }

    @SuppressWarnings("rawtypes")
    public static int[] batchSave(List<? extends Model> data, int batchSize) {
        Model model = data.get(0);
        Map<String, Object> attrs = Reflect.on(model).field("attrs").get();
        Class<? extends Model> modelClass = model.getClass();
        TableInfo tableInfo = TableInfoMapping.me().getTableInfo(modelClass);
        StringBuilder sql = new StringBuilder();
        List<Object> paras = Lists.newArrayList();
        DbKit.getDialect().forModelSave(tableInfo, attrs, sql, paras);
        Object[][] batchPara = new Object[data.size()][attrs.size()];
        for (int i = 0; i < data.size(); i++) {
            int j = 0;
            for (String key : attrs.keySet()) {
                batchPara[i][j++] = data.get(i).get(key);
            }
        }
        return Db.batch(sql.toString(), batchPara, batchSize);
    }

    public static void hashCode(Model model) {
        // TODO
    }

    public static void equals(Model model) {
        // TODO

    }
}
