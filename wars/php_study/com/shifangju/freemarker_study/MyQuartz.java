package com.shifangju.freemarker_study;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;

public class MyQuartz {

	public static void test(){
		  Scheduler scheduler;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			
			JobDetail job = JobBuilder.newJob(MyJob.class)
				      .withIdentity("job1", "group1")
				      .build();

		    // Trigger the job to run now, and then repeat every 40 seconds
			/*
		    Trigger trigger = TriggerBuilder.newTrigger()
						       .withIdentity("trigger1", "group1")
						       .startNow()
						       .withSchedule(
						    		   SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever())
						       .build();
			*/
			Trigger trigger = TriggerBuilder.newTrigger()
				    .withIdentity("trigger3", "group1")
				    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
				    .forJob("job1", "group1")
				    .build();
				  // Tell quartz to schedule the job using our trigger
				  scheduler.scheduleJob(job, trigger);
				  
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
