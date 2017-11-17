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

import yjt.model.Contract;
import yjt.model.Contract.Status;
import yjt.model.query.ContractQuery;

public class ContractCheckJob implements Job{
	protected static final Log log = Log.getLog(ContractCheckJob.class);
	
	public static void init(Scheduler scheduler) {
		
		JobDetail job = JobBuilder.newJob(ContractCheckJob.class)
			      .withIdentity("ContractCheckJob", "Check")
			      .build();

		//每天零时检查用户手机号码是否认证
		Trigger trigger = TriggerBuilder.newTrigger()
			    						.withIdentity("ContractCheckJob", "Check")
			    						.withSchedule(CronScheduleBuilder.cronSchedule("0 5 17 * * ?"))
			    						.forJob("ContractCheckJob", "Check")
			    						.build();
		// Tell quartz to schedule the job using our trigger
		try {
			scheduler.scheduleJob(job, trigger);
			log.info("ContractCheckJob(用于检查借条的状态) 启动成功");
		} catch (SchedulerException e) {
			// e.printStackTrace();
			log.error("ContractCheckJob(用于检查借条的状态) 初始化失败");
		}
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		Status[] stats = {Status.ESTABLISH};
		List<Contract> ary = ContractQuery.me().queryContracts(null, null, stats);
		if(ary != null && ary.size() > 0) {
			long curTime = System.currentTimeMillis();
			for(Contract contract : ary) {
				Date maturity_date = contract.getMaturityDate();
				if(maturity_date == null) {
					log.error("错误：contract(id = " + contract.getId().toString() + ") 的还款日错误");
					continue;
				}
				//已逾期
				if(curTime > maturity_date.getTime()) {
					contract.setStatus(Contract.Status.EXTEND.getIndex());
					contract.update();
					log.info("信息：contract(id = " + contract.getId().toString() + ") 逾期，已设为逾期状态");
				}
			}
		}
	}
}
