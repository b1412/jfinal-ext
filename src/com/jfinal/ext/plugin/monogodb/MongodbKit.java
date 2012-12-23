package com.jfinal.ext.plugin.monogodb;

import com.jfinal.log.Logger;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongodbKit {

    protected static Logger logger = Logger.getLogger(MongodbKit.class);

    private static Mongo    mongo  = null;
    private static DB       db     = null;

    public static DB getDB() {
        return db;
    }

    public static DBCollection getDBCollection(String name) {
        return db.getCollection(name);
    }

    public static void init(Mongo mongo, DB db) {
        MongodbKit.mongo = mongo;
        MongodbKit.db = db;

    }

    public static Mongo getMongo() {
        return mongo;
    }

    public static void setMongo(Mongo mongo) {
        MongodbKit.mongo = mongo;
    }

    public static DB getDb() {
        return db;
    }

    public static void setDb(DB db) {
        MongodbKit.db = db;
    }

}
