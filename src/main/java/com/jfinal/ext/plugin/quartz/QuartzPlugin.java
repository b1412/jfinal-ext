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
package com.jfinal.ext.plugin.quartz;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.jfinal.ext.kit.Reflect;
import com.jfinal.ext.kit.ResourceKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class QuartzPlugin implements IPlugin {
    public static final String VERSION_1 = "1";
    private static final String JOB = "job";
    private final Logger logger = Logger.getLogger(getClass());
    private Map<Job, String> jobs = Maps.newLinkedHashMap();
    private String version;
    private SchedulerFactory sf;
    private Scheduler scheduler;
    private String jobConfig;
    private String confConfig;
    private Map<String, String> jobProp;

    public QuartzPlugin(String jobConfig, String confConfig) {
        this.jobConfig = jobConfig;
        this.confConfig = confConfig;
    }

    public QuartzPlugin(String jobConfig) {
        this.jobConfig = jobConfig;
    }

    public QuartzPlugin() {
    }

    public QuartzPlugin add(String jobCronExp, Job job) {
        jobs.put(job, jobCronExp);
        return this;
    }

    @Override
    public boolean start() {
        loadJobsFromProperties();
        startJobs();
        return true;
    }

    private void startJobs() {
        try {
            if (StrKit.notBlank(confConfig)) {
                sf = new StdSchedulerFactory(confConfig);
            } else {
                sf = new StdSchedulerFactory();
            }
            scheduler = sf.getScheduler();
        } catch (SchedulerException e) {
            Throwables.propagate(e);
        }
        Set<Map.Entry<Job, String>> set = jobs.entrySet();
        for (Map.Entry<Job, String> entry : set) {
            Job job = entry.getKey();
            String jobClassName = job.getClass().getName();
            String jobCronExp = entry.getValue();
            JobDetail jobDetail;
            CronTrigger trigger;
            //JobDetail and CornTrigger are classes in 1.x version,but are interfaces in 2.X version.
            if (VERSION_1.equals(version)) {
                jobDetail = Reflect.on("org.quartz.JobDetail").create(jobClassName, jobClassName, job.getClass()).get();
                trigger = Reflect.on("org.quartz.CronTrigger").create(jobClassName, jobClassName, jobCronExp).get();
            } else {
                jobDetail = Reflect.on("org.quartz.JobBuilder").call("newJob", job.getClass()).call("withIdentity", jobClassName, jobClassName)
                        .call("build").get();
                Object temp = Reflect.on("org.quartz.TriggerBuilder").call("newTrigger").get();
                temp = Reflect.on(temp).call("withIdentity", jobClassName, jobClassName).get();
                temp = Reflect.on(temp).call("withSchedule",
                        Reflect.on("org.quartz.CronScheduleBuilder").call("cronSchedule", jobCronExp).get())
                        .get();
                trigger = Reflect.on(temp).call("build").get();
            }
            Date ft = Reflect.on(scheduler).call("scheduleJob", jobDetail, trigger).get();
            logger.debug(Reflect.on(jobDetail).call("getKey") + " has been scheduled to run at: " + ft + " " +
                    "and repeat based on expression: " + Reflect.on(trigger).call("getCronExpression"));
        }
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            Throwables.propagate(e);
        }
    }

    private void loadJobsFromProperties() {
        if (StrKit.isBlank(jobConfig)) {
            return;
        }
        jobProp = ResourceKit.readProperties(jobConfig);
        Set<Map.Entry<String, String>> entries = jobProp.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            if (!key.endsWith(JOB) || !isEnableJob(enable(key))) {
                continue;
            }
            String jobClassName = jobProp.get(key) + "";
            String jobCronExp = jobProp.get(cronKey(key)) + "";
            Class<Job> job = Reflect.on(jobClassName).get();
            try {
                jobs.put(job.newInstance(), jobCronExp);
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

    public QuartzPlugin version(String version) {
        this.version = version;
        return this;
    }

    private boolean isEnableJob(String enableKey) {
        Object enable = jobProp.get(enableKey);
        if (enable != null && "false".equalsIgnoreCase((enable + "").trim())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean stop() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            Throwables.propagate(e);
        }
        return true;
    }

    public QuartzPlugin confConfig(String confConfig) {
        this.confConfig = confConfig;
        return this;
    }

    public QuartzPlugin jobConfig(String jobConfig) {
        this.jobConfig = jobConfig;
        return this;
    }
}
