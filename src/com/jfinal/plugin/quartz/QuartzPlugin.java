package com.jfinal.plugin.quartz;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.jfinal.plugin.IPlugin;

public class QuartzPlugin implements IPlugin {
	private SchedulerFactory sf = null;
	private Scheduler sched = null;
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
			new RuntimeException(e); 
		}
		loadProperties();
		Enumeration enums = properties.keys();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement() + "";
			if (!key.endsWith("job")) {
				continue;
			}
			String cronKey = key.substring(0, key.lastIndexOf("job")) + "cron";
			String enable = key.substring(0, key.lastIndexOf("job")) + "enable";
			if (!isEnableJob(enable)) {
				continue;
			} 
			String jobClassName = properties.get(key) + "";
			String jobCronExp = properties.getProperty(cronKey) + "";
			Class clazz;
			try {
				clazz = Class.forName(jobClassName);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
	        JobDetail job = new JobDetail(jobClassName, jobClassName, clazz);
//			JobDetail job = newJob(clazz).withIdentity(jobClassName, jobClassName).build();
			CronTrigger trigger = null;
			try {
				trigger = new CronTrigger(jobClassName,jobClassName, jobCronExp);
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
			
//			CronTrigger trigger = newTrigger().withIdentity(jobClassName, jobClassName)
//					.withSchedule(cronSchedule(jobCronExp)).build();
			Date ft = null;
			try {
				sched.addJob(job, true);
				ft = sched.scheduleJob(trigger);
//				ft = sched.scheduleJob(job, trigger);
				sched.start();
			} catch (SchedulerException e) {
				new RuntimeException(e);
			}
			System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
					+ trigger.getCronExpression());
		}
		return true;
	} 


	private boolean isEnableJob(String enableKey) {
		Object enable = properties.get(enableKey);
		if(enable!=null&&"false".equalsIgnoreCase((enable+"").trim())){
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
			throw new RuntimeException(e);
		}
		System.out.println("------------load Propteries---------------");
		System.out.println(properties);
		System.out.println("------------------------------------------");
	}

	@Override
	public boolean stop() {
		try {
			sched.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
