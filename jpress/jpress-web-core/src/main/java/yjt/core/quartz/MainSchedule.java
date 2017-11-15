package yjt.core.quartz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.jfinal.kit.PathKit;
import com.mchange.io.FileUtils;

import yjt.core.quartz.jobs.ContractCheckJob;
import yjt.core.quartz.jobs.ContractSmsJob;

public class MainSchedule {
	
	public static void init(){		
		if(shouldStart() == false) return;
		Scheduler scheduler;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			
			//订单状态检查
			ContractCheckJob.init(scheduler);
			//发送催收短信
			ContractSmsJob.init(scheduler);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static boolean shouldStart() {
		boolean ret = false;
		String rootPath = PathKit.getRootClassPath();
		File f = new File(rootPath + "/quartz.lock");
		long cur = System.currentTimeMillis();
		if(f.exists()) {
			try {
				String timeStr = FileUtils.getContentsAsString(f);
				long time = Long.valueOf(timeStr);
				
				if(time + 30*1000 > cur) {
					ret = false;
				} else {
					FileOutputStream fos = new FileOutputStream(f);
					String toWrite = "" + cur;
					fos.write(toWrite.getBytes());
					fos.flush();
					fos.close();
					ret = true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(f);
				String toWrite = "" + cur;
				fos.write(toWrite.getBytes());
				fos.flush();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ret = true;
		}
		
		return ret;
	}
	
}
