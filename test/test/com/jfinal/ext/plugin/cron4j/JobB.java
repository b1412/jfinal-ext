package test.com.jfinal.ext.plugin.cron4j;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JobB implements Runnable {
    static int callTime = 0;

    @Override
    public void run() {
        callTime++;
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " JobB works,callTime is: " + callTime);
    }

}
