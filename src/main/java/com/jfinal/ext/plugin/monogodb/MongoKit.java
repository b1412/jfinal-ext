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

public class MongoKit {

    protected static Logger logger = Logger.getLogger(MongoKit.class);

    private static MongoClient client;
    private static DB defaultDb;

    public static void init(MongoClient client, String database) {
        MongoKit.client = client;
        MongoKit.defaultDb = client.getDB(database);

    }

    public static void updateFirst(String collectionName, Map<String, Object> q, Map<String, Object> o) {
        MongoKit.getCollection(collectionName).findAndModify(toDBObject(q), toDBObject(o));
    }

    public static int removeAll(String collectionName) {
        return MongoKit.getCollection(collectionName).remove(new BasicDBObject()).getN();
    }

    public static int remove(String collectionName, Map<String, Object> filter) {
        return MongoKit.getCollection(collectionName).remove(toDBObject(filter)).getN();
    }

    public static int save(String collectionName, List<Record> records) {
        List<DBObject> objs = new ArrayList<DBObject>();
        for (Record record : records) {
            objs.add(toDbObject(record));
        }
        return MongoKit.getCollection(collectionName).insert(objs).getN();

    }

    public static int save(String collectionName, Record record) {
        return MongoKit.getCollection(collectionName).save(toDbObject(record)).getN();
    }

    public static Record findFirst(String collectionName) {
        return toRecord(MongoKit.getCollection(collectionName).findOne());
    }

    public static Page<Record> paginate(String collection, int pageNumber, int pageSize) {
        return paginate(collection, pageNumber, pageSize, null, null, null);
    }

    public static Page<Record> paginate(String collection, int pageNumber, int pageSize, Map<String, Object> filter) {
        return paginate(collection, pageNumber, pageSize, filter, null, null);
    }

    public static Page<Record> paginate(String collection, int pageNumber, int pageSize, Map<String, Object> filter,
            Map<String, Object> like) {
        return paginate(collection, pageNumber, pageSize, filter, like, null);
    }

    public static Page<Record> paginate(String collection, int pageNumber, int pageSize, Map<String, Object> filter,
            Map<String, Object> like, Map<String, Object> sort) {
        DBCollection logs = MongoKit.getCollection(collection);
        BasicDBObject conditons = new BasicDBObject();
        buildFilter(filter, conditons);
        buildLike(like, conditons);
        DBCursor dbCursor = logs.find(conditons);
        page(pageNumber, pageSize, dbCursor);
        sort(sort, dbCursor);
        List<Record> records = new ArrayList<Record>();
        while (dbCursor.hasNext()) {
            records.add(toRecord(dbCursor.next()));
        }
        int totalRow = dbCursor.count();
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

    private static void page(int pageNumber, int pageSize, DBCursor dbCursor) {
        dbCursor = dbCursor.skip((pageNumber - 1) * pageSize).limit(pageSize);
    }

    private static void sort(Map<String, Object> sort, DBCursor dbCursor) {
        if (sort != null) {
            DBObject dbo = new BasicDBObject();
            Set<Entry<String, Object>> entrySet = sort.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                String key = entry.getKey();
                Object val = entry.getValue();
                dbo.put(key, "asc".equalsIgnoreCase(val + "") ? 1 : -1);
            }
            dbCursor = dbCursor.sort(dbo);
        }
    }

    private static void buildLike(Map<String, Object> like, BasicDBObject conditons) {
        if (like != null) {
            Set<Entry<String, Object>> entrySet = like.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                String key = entry.getKey();
                Object val = entry.getValue();
                conditons.put(key, MongoKit.getLikeStr(val));
            }
        }
    }

    private static void buildFilter(Map<String, Object> filter, BasicDBObject conditons) {
        if (filter != null) {
            Set<Entry<String, Object>> entrySet = filter.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                String key = entry.getKey();
                Object val = entry.getValue();
                conditons.put(key, val);
            }

        }
    }

    @SuppressWarnings("unchecked")
    public static Record toRecord(DBObject dbObject) {
        Record record = new Record();
        record.setColumns(dbObject.toMap());
        return record;
    }

    public static BasicDBObject getLikeStr(Object findStr) {
        Pattern pattern = Pattern.compile("^.*" + findStr + ".*$", Pattern.CASE_INSENSITIVE);
        return new BasicDBObject("$regex", pattern);
    }

    public static DB getDB() {
        return defaultDb;
    }

    public static DB getDB(String dbName) {
        return client.getDB(dbName);
    }

    public static DBCollection getCollection(String name) {
        return defaultDb.getCollection(name);
    }

    public static DBCollection getDBCollection(String dbName, String collectionName) {
        return getDB(dbName).getCollection(collectionName);
    }

    public static MongoClient getClient() {
        return client;
    }

    public static void setMongoClient(MongoClient client) {
        MongoKit.client = client;
    }

    private static BasicDBObject toDBObject(Map<String, Object> map) {
        BasicDBObject dbObject = new BasicDBObject();
        Set<Entry<String, Object>> entrySet = map.entrySet();
        for (Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object val = entry.getValue();
            dbObject.append(key, val);
        }
        return dbObject;
    }

    private static BasicDBObject toDbObject(Record record) {
        BasicDBObject object = new BasicDBObject();
        for (Entry<String, Object> e : record.getColumns().entrySet()) {
            object.append(e.getKey(), e.getValue());
        }
        return object;
    }
}
