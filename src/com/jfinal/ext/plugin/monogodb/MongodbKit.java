package com.jfinal.ext.plugin.monogodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongodbKit {

    protected static Logger    logger = Logger.getLogger(MongodbKit.class);

    private static MongoClient client = null;
    private static DB          db     = null;

    public static DB getDB() {
        return db;
    }

    public static DB getDB(String dbName) {
        return client.getDB(dbName);
    }

    public static DBCollection getDBCollection(String name) {
        return db.getCollection(name);
    }

    public static DBCollection getDBCollection(String dbName, String collectionName) {
        return getDB(dbName).getCollection(collectionName);
    }

    public static void init(MongoClient client, DB db) {
        MongodbKit.client = client;
        MongodbKit.db = db;

    }

    public static Record findFirst(String collectionName) {
        return toRecord(MongodbKit.getDBCollection(collectionName).findOne());
    }

    public static Page<Record> paginate(String collection, int pageNumber, int pageSize) {
        return paginate(collection, pageNumber, pageSize, null, null, null);
    }

    public static Page<Record> paginate(String collection, int pageNumber, int pageSize, Map<String, String> filter) {
        return paginate(collection, pageNumber, pageSize, filter, null, null);
    }

    public static Page<Record> paginate(String collection, int pageNumber, int pageSize, Map<String, String> filter,
                                        Map<String, String> like) {
        return paginate(collection, pageNumber, pageSize, filter, like, null);
    }

    public static Page<Record> paginate(String collection, final int pageNumber, final int pageSize,
                                        Map<String, String> filter, Map<String, String> like,
                                        final Map<String, String> sort) {
        DBCollection logs = MongodbKit.getDBCollection(collection);
        BasicDBObject conditons = new BasicDBObject();
        if (filter != null) {
            Set<Entry<String, String>> entrySet = filter.entrySet();
            for (Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String val = entry.getValue();
                conditons.put(key, val);
            }

        }
        if (like != null) {
            Set<Entry<String, String>> entrySet = like.entrySet();
            for (Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String val = entry.getValue();
                conditons.put(key, MongodbKit.getLikeStr(val));
            }
        }

        DBCursor dbCursor = logs.find(conditons);
        int totalRow =  dbCursor.count();
        dbCursor = dbCursor.skip((pageNumber - 1) * pageSize).limit(pageSize);
        if (sort != null) {
            DBObject dbo = new BasicDBObject();
            Set<Entry<String, String>> entrySet = sort.entrySet();
            for (Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String val = entry.getValue();
                dbo.put(key, val.equalsIgnoreCase("asc") ? 1 : -1);
            }
            dbCursor = dbCursor.sort(dbo);
        }
        List<Record> records = new ArrayList<Record>();
        while (dbCursor.hasNext()) {
            records.add(toRecord(dbCursor.next()));
        }
        if (totalRow <= 0) {
            return new Page<Record>(new ArrayList<Record>(0), pageNumber, pageSize, 0, 0);
        }
        int totalPage = totalRow / pageSize;
        if (totalRow % pageSize != 0) {
            totalPage++;
        }
        Page<Record> page = new Page<Record>(records, pageNumber, pageSize, totalPage, totalRow);
        return page;
    }

    @SuppressWarnings("unchecked")
    public static Record toRecord(DBObject dbObject) {
        Record record = new Record();
        record.setColumns(dbObject.toMap());
        return record;
    }

    public static BasicDBObject getLikeStr(String findStr) {
        Pattern pattern = Pattern.compile("^.*" + findStr + ".*$", Pattern.CASE_INSENSITIVE);
        return new BasicDBObject("$regex", pattern);
    }

    public static MongoClient getClient() {
        return client;
    }

    public static void setMongoClient(MongoClient client) {
        MongodbKit.client = client;
    }

    public static DB getDb() {
        return db;
    }

    public static void setDb(DB db) {
        MongodbKit.db = db;
    }

}
