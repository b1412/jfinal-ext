package com.jfinal.ext.plugin.config;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import com.jfinal.log.Logger;

public class ConfigKit {

    protected static Logger logger = Logger.getLogger(ConfigKit.class);

    private static List<String> includeResources;

    private static boolean reload = true;

    private static List<String> excludeResources;

    private static Map<String, String> map;

    private static Map<String, String> testMap;

    private static String classpath;

    private static Map<String, Long> lastmodifies = new HashMap<String, Long>();

    /**
     * @param includeResources
     * @param excludeResources
     * @param reload
     */
    static void init(List<String> includeResources, List<String> excludeResources, boolean reload) {
        ConfigKit.includeResources = includeResources;
        ConfigKit.excludeResources = excludeResources;
        ConfigKit.reload = reload;
        classpath = ConfigKit.class.getClassLoader().getResource("").getFile();
        map = new HashMap<String, String>();
        testMap = new HashMap<String, String>();
        for (final String resource : includeResources) {
            logger.debug("include :" + resource);
            File[] propertiesFiles = null;
            propertiesFiles = new File(classpath).listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return Pattern.compile(resource).matcher(pathname.getName()).matches();
                }
            });
            for (File file : propertiesFiles) {
                String fileName = file.getAbsolutePath();
                logger.debug("fileName:" + fileName);
                if (fileName.endsWith("-test." + ConfigPlugin.getSuffix())) {
                    continue;
                }
                boolean excluded = false;
                for (final String exclude : excludeResources) {
                    if (Pattern.compile(exclude).matcher(file.getName()).matches()) {
                        excluded = true;
                    }
                }
                if (excluded) {
                    continue;
                }
                Properties prop = new Properties();
                InputStream is;
                try {
                    is = new FileInputStream(fileName);
                    prop.load(is);
                    lastmodifies.put(fileName, new File(fileName).lastModified());
                } catch (FileNotFoundException e) {
                    logger.error(e.getMessage(), e);
                    continue;
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    continue;
                }
                Set<Object> keys = prop.keySet();
                for (Object key : keys) {
                    map.put(encoding(key + "") + "", encoding(prop.getProperty(key + "", "")));
                }
                String testFileName = fileName.substring(0, fileName.indexOf("." + ConfigPlugin.getSuffix())) + "-test."
                        + ConfigPlugin.getSuffix();
                Properties tprop = new Properties();
                try {
                    InputStream tis = new FileInputStream(testFileName);
                    tprop.load(tis);
                } catch (FileNotFoundException e) {
                    logger.debug("the file" + fileName + "has no test file.");
                    continue;
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    continue;
                }
                Set<Object> tkeys = prop.keySet();
                for (Object tkey : tkeys) {
                    testMap.put(encoding(tkey + "") + "", encoding(tprop.getProperty(tkey + "", "")));
                }
            }
        }
        logger.debug("map" + map);
        logger.debug("testMap" + testMap);
        logger.info("config init success!");
    }

    public static String getStr(String key, String defaultVal) {
        if (testMap == null || map == null) {
            throw new RuntimeException(" please start ConfigPlugin first~");
        }
        if (reload) {
            checkFileModify();
        }
        String val = testMap.get(key);
        if (val == null || "".equals(val.trim())) {
            val = map.get(key);
        }
        return val == null ? defaultVal : val + "";

    }

    private static void checkFileModify() {
        Set<String> filenames = lastmodifies.keySet();
        for (String filename : filenames) {
            long lastmodify = lastmodifies.get(filename);
            File file = new File(filename);
            if (lastmodify != file.lastModified()) {
                logger.info(filename + " changed, reload.");
                init(includeResources, excludeResources, reload);
            }
        }
    }

    public static String getStr(String key) {
        return getStr(key, "");
    }

    public static long getLong(String key) {
        return getLong(key, 0);
    }

    public static long getLong(String key, long defaultVal) {
        String val = getStr(key).trim();
        if ("".equals(val)) {
            return defaultVal;
        }
        return Long.parseLong(val);
    }

    public static int getInt(String key, int defaultVal) {
        String val = getStr(key).trim();
        if ("".equals(val)) {
            return defaultVal;
        }
        return Integer.parseInt(val);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static void set(String key, String val) {

    }

    private static String encoding(String val) {
        try {
            val = new String(val.getBytes("ISO8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn(e.getMessage(), e);
        }
        return val;
    }
}
