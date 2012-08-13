package com.jfinal.plugin.cron;

import it.sauronsoftware.cron4j.Scheduler;

import com.jfinal.plugin.IPlugin;

public class Cron4jPlugin implements IPlugin {
	private final Scheduler scheduler = new Scheduler();

	@Override
	public boolean start() {
		scheduler.start();
		return true;
	}

	@Override
	public boolean stop() {
		scheduler.stop();
		return true;
	}

	public void addTask(String cronExpress, Runnable task) {
		scheduler.schedule(cronExpress, task);
	}

}
