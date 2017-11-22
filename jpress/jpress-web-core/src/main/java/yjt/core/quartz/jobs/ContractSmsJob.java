package yjt.core.quartz.jobs;

import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

import io.jpress.model.User;
import yjt.Utils;
import yjt.YuxinSms;
import yjt.model.Contract;
import yjt.model.Contract.Status;
import yjt.model.query.ContractQuery;

public class ContractSmsJob implements Job{
	protected static final Log log = Log.getLog(ContractSmsJob.class);
	
	public static void init(Scheduler scheduler) {
		
		JobDetail job = JobBuilder.newJob(ContractSmsJob.class)
			      .withIdentity("ContractSmsJob", "Check")
			      .build();

		//每天零时检查用户手机号码是否认证
		Trigger trigger = TriggerBuilder.newTrigger()
			    						.withIdentity("ContractSmsJob", "Check")
			    						.withSchedule(CronScheduleBuilder.cronSchedule("0 35 11 * * ?"))
			    						.forJob("ContractSmsJob", "Check")
			    						.build();
		// Tell quartz to schedule the job using our trigger
		try {
			scheduler.scheduleJob(job, trigger);
			log.info("ContractSmsJob(催收短信程序) 启动成功");
		} catch (SchedulerException e) {
			// e.printStackTrace();
			log.error("ContractSmsJob(催收短信程序) 初始化失败");
		}
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		Status[] stats = {Status.ESTABLISH};
		List<Contract> ary = ContractQuery.me().queryContracts(null, null, stats);
		if(ary != null && ary.size() > 0) {
			long curTime00 = Utils.getTodayStartTime();
			for(Contract contract : ary) {
				Date maturity_date = contract.getMaturityDate();
				if(maturity_date == null) {
					log.error("错误：contract(id = " + contract.getId().toString() + ") 的还款日不存在");
					continue;
				}
				boolean flag = false;
				//只对未逾期的订单发送催收短信
				if(curTime00  < maturity_date.getTime() && curTime00 + 1L * 86400 * 1000 >= maturity_date.getTime()) {
					flag = true;
				}
				if(flag == true) {
					log.info("信息：向(id = " + contract.getId().toString() + ") 的借款人发送催收短信");
					//发送催收短信
					User debit = contract.getDebitUser();
					if(debit != null && StrKit.notBlank(debit.getMobile())) {
						//您有${money}元借款将于${date1}到期，请在到期日${date2}前充值到账户余额还款。
						JSONObject json = new JSONObject();
						json.put("money", String.format("%.2f", contract.getAmount().doubleValue()));
						json.put("date1", Utils.toYmd(contract.getMaturityDate()));
						Date prev = Utils.getPrevDay(contract.getMaturityDate());
						json.put("date2", Utils.toYmd(prev) + " 22:00");
						new YuxinSms(debit.getMobile(), json.toJSONString(), "414215").send();
					}
				}
			}
		}
	}
}
