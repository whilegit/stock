package yjt.api.v1.Push;

import yjt.core.push.PushMessage;

public class BalanceAddPush extends PushMessage{

	public BalanceAddPush(String targetDevice, String fromUser, double money) {
		super();
		this.setDeviceType("ALL");
		this.setTargetDevice(targetDevice);
		this.setPushType("NOTICE");
		
		this.setTitle("转账消息");
		this.setBody(fromUser + "向您转账" + String.format("%.2f", money) + "元，现已到账");
		
		this.setiOSBadge(1);
		
		this.setAndroidOpenType("APPLICATION");
		this.setAndroidActivity("");
		this.setAndroidOpenUrl("");
		this.setAndroidNotifyType("BOTH");
	}
}
