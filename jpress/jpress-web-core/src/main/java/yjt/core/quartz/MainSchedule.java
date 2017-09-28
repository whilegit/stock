package yjt.core.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import yjt.core.quartz.jobs.PhoneAuthCheckJob;


public class MainSchedule {
	
	public static void init(){
		
		Scheduler scheduler;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			
			//手机是否验证是否过期检验
			PhoneAuthCheckJob.init(scheduler);
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
