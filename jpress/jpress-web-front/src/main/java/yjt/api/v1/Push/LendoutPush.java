package yjt.api.v1.Push;

import yjt.core.push.PushMessage;

public class LendoutPush extends PushMessage{

	public LendoutPush(String targetDevice, String toUser, double money) {
		super();
		this.setDeviceType("ALL");
		this.setTargetDevice(targetDevice);
		this.setPushType("NOTICE");
		
		this.setTitle("转账消息");
		this.setBody("您向"+toUser+"的转账已成功，转账金额为" + String.format("%.2f", money) + "元");
		
		this.setiOSBadge(1);
		
		this.setAndroidOpenType("APPLICATION");
		this.setAndroidActivity("");
		this.setAndroidOpenUrl("");
		this.setAndroidNotifyType("BOTH");
	}
}
