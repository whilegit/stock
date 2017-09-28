package yjt.core.quartz.jobs;

import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.jfinal.log.Log;

import io.jpress.model.User;
import io.jpress.model.query.UserQuery;

public class PhoneAuthCheckJob implements Job{
	protected static final Log log = Log.getLog(PhoneAuthCheckJob.class);
	
	public static void init(Scheduler scheduler) {
		
		JobDetail job = JobBuilder.newJob(PhoneAuthCheckJob.class)
			      .withIdentity("PhoneAuthCheckJob", "Check")
			      .build();

		//每天零时检查用户手机号码是否认证
		Trigger trigger = TriggerBuilder.newTrigger()
			    						.withIdentity("PhoneAuthCheckTrigger", "Check")
			    						.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
			    						.forJob("PhoneAuthCheckJob", "Check")
			    						.build();
		// Tell quartz to schedule the job using our trigger
		try {
			scheduler.scheduleJob(job, trigger);
			log.info("PhoneAuthCheckJob(用于检查用户的手机号码是否验证过期) 启动成功");
		} catch (SchedulerException e) {
			// e.printStackTrace();
			log.error("PhoneAuthCheckJob(用于检查用户的手机号码是否验证过期) 初始化失败");
		}
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		long count = UserQuery.me().findCount();
		if(count == 0) return;
		int cluster = 100;
		for(int i = 0; i< count / cluster + 1; i++) {
			List<User> users = UserQuery.me().findList(i, cluster, null, "visitor", "normal", "id");
			if(users == null || users.size() == 0) break;
			for(User u : users) {
				//Date expire = u.getAuthExpire();
			}
		}
	}
}
