package test.com.jfinal.plugin.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class JobA implements Job {
	static int callTime = 0;
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		callTime++;
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" JobA works,callTime is: "+callTime);
	}


}
