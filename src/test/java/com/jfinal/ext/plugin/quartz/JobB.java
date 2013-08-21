package com.jfinal.ext.plugin.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobB implements Job {
    int [] ii = new int[]{};
    static int callTime = 0;
    static int l = 0;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        int i =0,j= 0;
        callTime++;
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " JobB works,callTime is: " + callTime);
        String [] arrs ;
        String arrs2 [] ;
    }

}
