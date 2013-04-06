package com.jfinal.ext.plugin.cron;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

public class Cron4jPlugin implements IPlugin {
    private static final String JOB = "job";
    protected final Logger log = Logger.getLogger(getClass());

    private String config = "job.properties";

    private Scheduler scheduler;
    private Properties properties;

    public Cron4jPlugin(String config) {
        this.config = config;
    }

    public Cron4jPlugin() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean start() {
        scheduler = new Scheduler();
        loadProperties();
        Enumeration<Object> enums = properties.keys();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement() + "";
            if (!key.endsWith(JOB) || !isEnableJob(enable(key))) {
                continue;
            }
            String jobClassName = properties.get(key) + "";
            String jobCronExp = properties.getProperty(cronKey(key)) + "";
            Class<Runnable> clazz;
            try {
                clazz = (Class<Runnable>) Class.forName(jobClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("job config error", e);
            }
            try {
                scheduler.schedule(jobCronExp, clazz.newInstance());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
            log.debug(jobClassName + " has been scheduled to run and repeat based on expression: " + jobCronExp);
        }
        scheduler.start();
        return true;
    }

    private String enable(String key) {
        return key.substring(0, key.lastIndexOf(JOB)) + "enable";
    }

    private String cronKey(String key) {
        return key.substring(0, key.lastIndexOf(JOB)) + "cron";
    }

    private boolean isEnableJob(String enableKey) {
        Object enable = properties.get(enableKey);
        if (enable != null && "false".equalsIgnoreCase((enable + "").trim())) {
            return false;
        }
        return true;
    }

    private void loadProperties() {
        properties = new Properties();
        log.debug("config is: " + config);
        InputStream is = Cron4jPlugin.class.getClassLoader().getResourceAsStream(config);
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.debug("------------load Propteries---------------");
        log.debug(properties.toString());
        log.debug("------------------------------------------");
    }

    @Override
    public boolean stop() {
        scheduler.stop();
        return true;
    }
}
