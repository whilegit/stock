package yjt.api.v1.Push;

import java.util.Date;

import yjt.Utils;
import yjt.core.push.PushMessage;

public class ChargeFailPush extends PushMessage{
	
	public ChargeFailPush(String targetDevice, Date date, double money) {
		super();
		this.setDeviceType("ALL");
		this.setTargetDevice(targetDevice);
		this.setPushType("NOTICE");
		
		this.setTitle("账户充值未成功");
		this.setBody("您于"+Utils.toYmdHmsChs(date)+"进行的账户充值，金额：" + String.format("%.2f", money) + "元，充值未成功。相关原因请查看详情。");
		
		this.setiOSBadge(1);
		
		this.setAndroidOpenType("APPLICATION");
		this.setAndroidActivity("");
		this.setAndroidOpenUrl("");
		this.setAndroidNotifyType("BOTH");
	}
}
