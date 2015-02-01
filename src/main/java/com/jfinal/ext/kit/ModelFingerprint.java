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

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.*;
import org.apache.commons.codec.binary.Hex;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public abstract class ModelFingerprint<M extends ModelFingerprint<M>> extends ModelExt<M> {

    private static Logger LOG = Logger.getLogger(ModelFingerprint.class);

    private String fingerprintColumnLabel = "fingerprint";

    private ConcurrentMap<String, Integer> map = null;

    private Set<Integer> deleting = null;

    private Set<String> CACHE = Sets.newHashSet();

    private String fingerprint;

    private Class<? extends ModelExt<M>> clazz;

    @SuppressWarnings("unchecked")
    public ModelFingerprint() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        clazz = (Class<? extends ModelExt<M>>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
    }

    public String tableName() {
        return TableMapping.me().getTable(this.getClass()).getName();
    }

    public void init() {
        map = Maps.newConcurrentMap();
        Table tableInfo = TableMapping.me().getTable(clazz);
        if (!tableInfo.hasColumnLabel(fingerprintColumnLabel)) {
            throw new ActiveRecordException("fingerprintColumnLabel (" + fingerprintColumnLabel + ") is not exist");
        }
        LOG.info("begin load " + tableName() + " fingerprints");
        List<Record> records = Db.find("SELECT id, " + fingerprintColumnLabel + " FROM " + tableName());
        for (Record record : records) {
            map.put(record.getStr(fingerprintColumnLabel), record.getInt("id"));
        }
        LOG.info("end load " + tableName() + " fingerprints");
        deleting = Sets.newHashSet(map.values());
        LOG.info("loaded " + deleting.size() + " data");
    }

    public abstract List<String> fingerprintColumns();

    public abstract ModelFingerprint fingerprinter();

    public String calcFingerprint() {
        if (fingerprint == null) {
            StringBuilder sb = new StringBuilder();
            for (String col : fingerprintColumns()) {
                sb.append(get(col));
                sb.append("|");
            }
            sb.deleteCharAt(sb.length() - 1);
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                fingerprint = Hex.encodeHexString(md.digest(sb.toString().getBytes()));
            } catch (Exception e) {
                fingerprint = "" + hashCode();
            }
        }
        return fingerprint;
    }

    public void compare(ModelFingerprint modelFingerprint) {
        if (map == null) {
            throw new IllegalStateException("modelFingerprint have not been initialized");
        }
        Integer id = map.get(modelFingerprint.calcFingerprint());
        if (id != null) {
            deleting.remove(id);
            modelFingerprint.set("id", id);
        } else if (CACHE.contains(modelFingerprint.getStr(fingerprintColumnLabel))) {
            modelFingerprint.set("id", -1);
        } else {
            CACHE.add(modelFingerprint.getStr(fingerprintColumnLabel));
        }
    }

    public ModelFingerprint addFingerprint() {
        set("fingerprint", calcFingerprint());
        return this;
    }

    public void saveWithFingerprint() {
        addFingerprint();
        fingerprinter().compare(this);
        if (get("id") == null) {
            save();
        }
    }

    public void removeExpired() {
        int size = deleting.size();
        LOG.info("delete " + size+ "records");
        if (size != 0) {
            List<Integer> ids = Lists.newArrayList(deleting);
            int idSize = ids.size();
            int batchSize = 1000;
            int length = idSize / batchSize;
            if (idSize % batchSize != 0) {
                length++;
            }
            for (int i = 0; i < length; i++) {
                if (i == length - 1) {
                    String sql = "DELETE FROM " + tableName() + " WHERE id IN ("
                            + Joiner.on(", ").join(ids.subList(i * batchSize, ids.size()), ", ") + ")";
                    Db.update(sql);
                } else {
                    String sql = "DELETE FROM " + tableName() + " WHERE id IN ("
                            + Joiner.on(", ").join(ids.subList(i * batchSize, i * batchSize + batchSize), ", ") + ")";
                    Db.update(sql);
                }
            }
        }
    }
}