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

/**
 * Created by kid on 14-4-24.
 */
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
            throw new ActiveRecordException("The fingerprintColumnLabel (" + fingerprintColumnLabel + ") is not exist");
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
        if(map == null){
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
        LOG.info("delete " + size);
        if (size != 0) {
            List<Integer> ids = Lists.newArrayList(deleting);
            int idsize = ids.size();
            int batchSize = 1000;
            int length = idsize / batchSize;
            if (idsize % batchSize != 0) {
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