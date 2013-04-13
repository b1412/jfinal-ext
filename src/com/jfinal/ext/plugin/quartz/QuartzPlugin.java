package com.jfinal.ext.plugin.quartz;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.joor.Reflect;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.google.common.base.Throwables;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

public class QuartzPlugin implements IPlugin {
    private static final String JOB = "job";

    protected final Logger logger = Logger.getLogger(getClass());

    private SchedulerFactory sf;
    private Scheduler sched;
    private String config = "job.properties";
    private Properties properties;

    public QuartzPlugin(String config) {
        this.config = config;
    }

    public QuartzPlugin() {
    }

    @Override
    public boolean start() {
        sf = new StdSchedulerFactory();
        try {
            sched = sf.getScheduler();
        } catch (SchedulerException e) {
            Throwables.propagate(e);
        }
        loadProperties();
        Enumeration<Object> enums = properties.keys();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement() + "";
            if (!key.endsWith(JOB) || !isEnableJob(enable(key))) {
                continue;
            }
            String jobClassName = properties.get(key) + "";
            String jobCronExp = properties.getProperty(cronKey(key)) + "";
            Class<Job> clazz = Reflect.on(jobClassName).get();
            JobDetail job = new JobDetail(jobClassName, jobClassName, clazz);
            CronTrigger trigger = null;
            try {
                trigger = new CronTrigger(jobClassName, jobClassName, jobCronExp);
            } catch (ParseException e) {
                Throwables.propagate(e);
            }
            Date ft = null;
            try {
                ft = sched.scheduleJob(job, trigger);
                sched.start();
            } catch (SchedulerException e) {
                Throwables.propagate(e);
            }
            logger.debug(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                    + trigger.getCronExpression());
        }
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
        InputStream is = QuartzPlugin.class.getClassLoader().getResourceAsStream(config);
        try {
            properties.load(is);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        logger.debug("------------load Propteries---------------");
        logger.debug(properties.toString());
        logger.debug("------------------------------------------");
    }

    @Override
    public boolean stop() {
        try {
            sched.shutdown();
        } catch (SchedulerException e) {
            Throwables.propagate(e);
        }
        return true;
    }

}
