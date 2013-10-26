/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.kit;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SuppressWarnings("serial")
public class KeyLabel implements Comparable<KeyLabel>, Serializable {
    private String key;
    private String label;

    public KeyLabel() {
        super();
    }

    public KeyLabel(String key, String label) {
        this.label = label;
        this.key = key;
    }

    public static Map<String, String> converListToMap(List<KeyLabel> list) {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (KeyLabel k : list) {
            map.put(k.getKey(), k.getLabel());
        }

        return map;
    }

    public static List<KeyLabel> converMapToList(Map<String,Object> map) {
        List<KeyLabel> list = Lists.newArrayList();
        Set<String> keys = map.keySet();
        for (Object key : keys) {
            KeyLabel keyLabel = new KeyLabel();
            keyLabel.setKey(key.toString());
            keyLabel.setLabel(map.get(key) + "");
            list.add(keyLabel);
        }
        return list;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String value) {
        this.key = value;
    }

    @Override
    public int compareTo(KeyLabel other) {
        return this.label.compareTo(other.getLabel());
    }

    @Override
    public String toString() {
        return "KeyLabel["+this.label+", "+this.key+"]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof KeyLabel)) {
            return false;
        }

        KeyLabel bean = (KeyLabel) obj;
        int nil = (this.getKey() == null) ? 1 : 0;
        nil += (bean.getKey() == null) ? 1 : 0;

        if (nil == 2) {
            return true;
        } else if (nil == 1) {
            return false;
        } else {
            return this.getKey().equals(bean.getKey());
        }
    }

    @Override
    public int hashCode() {
        return (this.getKey() == null) ? 17 : this.getKey().hashCode();
    }
}
