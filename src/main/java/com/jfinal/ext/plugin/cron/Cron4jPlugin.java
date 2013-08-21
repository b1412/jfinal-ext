package com.jfinal.ext.plugin.cron;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.joor.Reflect;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

public class Cron4jPlugin implements IPlugin {
    
    private static final String JOB = "job";

    private final Logger log = Logger.getLogger(getClass());

    private Map<String, Runnable> jobs = Maps.newHashMap();
    
    private String config = "job.properties";

    private Scheduler scheduler;
    
    private Properties properties;
    
    public Cron4jPlugin() {
    }

    public Cron4jPlugin add(String jobCronExp, Runnable job) {
        jobs.put(jobCronExp, job);
        return this;
    }

    public Cron4jPlugin config(String config) {
        this.config = config;
        return this;
    }
    
    @Override
    public boolean start() {
        scheduler = new Scheduler();
        loadJobsFromProperties();
        Set<Entry<String, Runnable>> set = jobs.entrySet();
        for (Entry<String, Runnable> entry : set) {
            scheduler.schedule(entry.getKey(), entry.getValue());
            log.debug(entry.getValue() + " has been scheduled to run and repeat based on expression: " + entry.getKey());
        }
        scheduler.start();
        return true;
    }

    private void loadJobsFromProperties() {
        loadProperties();
        Enumeration<Object> enums = properties.keys();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement() + "";
            if (!key.endsWith(JOB) || !isEnableJob(enable(key))) {
                continue;
            }
            String jobClassName = properties.get(key) + "";
            String jobCronExp = properties.getProperty(cronKey(key)) + "";
            Class<Runnable> clazz = Reflect.on(jobClassName).get();
            try {
                jobs.put(jobCronExp, clazz.newInstance());
            } catch (Exception e) {
                Throwables.propagate(e);
            }
        }
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
            Throwables.propagate(e);
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
