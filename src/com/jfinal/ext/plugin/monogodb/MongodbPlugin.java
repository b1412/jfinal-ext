package com.jfinal.ext.plugin.monogodb;

import java.net.UnknownHostException;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.mongodb.DB;
import com.mongodb.Mongo;

public class MongodbPlugin implements IPlugin {

    protected final Logger logger       = Logger.getLogger(getClass());

    private static String  DEFAULT_HOST = "loaclhost";
    private static int     DEFAUL_PORT  = 27017;

    private Mongo          mongo;
    private DB             db;
    private String         host;
    private int            port;
    private String         database;

    public MongodbPlugin(String database){
        this.host = DEFAULT_HOST;
        this.port = DEFAUL_PORT;
        this.database = database;
    }

    public MongodbPlugin(String host, int port, String database){
        this.host = host;
        this.port = port;
        this.database = database;
    }

    @Override
    public boolean start() {

        try {
            mongo = new Mongo(host, port);
            db = mongo.getDB(database);
        } catch (UnknownHostException e) {
            throw new RuntimeException("can't connect mongodb, please check the host and port:" + host + ", " + port, e);
        }

        MongodbKit.init(mongo, db);
        return true;
    }

    @Override
    public boolean stop() {
        if (mongo != null) {
            mongo.close();
            db = null;
        }
        return true;
    }

}
