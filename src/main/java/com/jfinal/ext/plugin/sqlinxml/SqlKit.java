package com.jfinal.ext.plugin.sqlinxml;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.ext.kit.JaxbKit;
import com.jfinal.log.Logger;

public class SqlKit {

    protected static final Logger LOG = Logger.getLogger(SqlKit.class);

    private static Map<String, String> sqlMap;

    public static String sql(String groupNameAndsqlId) {
        if (sqlMap == null) {
            throw new NullPointerException("SqlInXmlPlugin not start");
        }
        return sqlMap.get(groupNameAndsqlId);
    }

    static void clearSqlMap() {
        sqlMap.clear();
    }

    static void init() {
        sqlMap = new HashMap<String, String>();
        File file = new File(SqlKit.class.getClassLoader().getResource("").getFile());
        File[] files = file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith("sql.xml")) {
                    return true;
                }
                return false;
            }
        });
        for (File xmlfile : files) {
            SqlGroup group = JaxbKit.unmarshal(xmlfile, SqlGroup.class);
            String name = group.name;
            if (name == null || name.trim().equals("")) {
                name = xmlfile.getName();
            }
            for (SqlItem sqlItem : group.sqlItems) {
                sqlMap.put(name + "." + sqlItem.id, sqlItem.value);
            }
        }
        LOG.debug("sqlMap" + sqlMap);
    }
}
