package com.jfinal.ext.kit;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

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
}
