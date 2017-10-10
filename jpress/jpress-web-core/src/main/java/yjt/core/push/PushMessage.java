package yjt.core.push;

public class PushMessage {
	
	//目标设备
	private String deviceType;  //设备类型,ANDROID,iOS,ALL
	private String targetDevice;
	private String pushType;    //消息类型，NOTICE或MESSAGE
	
	//内容
	private String title;
	private String body;
	
	//iOS配置
	private int iOSBadge;  //iOS应用图标右上角角标
	
	//Android配置
	private String androidNotifyType;  //通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
	private String androidOpenType;    //点击通知后动作 "APPLICATION" : 打开应用 "ACTIVITY" : 打开AndroidActivity "URL" : 打开URL "NONE" : 无跳转
	private String androidOpenUrl;     //当androidOpenType==URL时有效
	private String androidActivity;    //当androidOpenType==ACTIVITY时有效
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getTargetDevice() {
		return targetDevice;
	}
	public void setTargetDevice(String targetDevice) {
		this.targetDevice = targetDevice;
	}
	public String getPushType() {
		return pushType;
	}
	public void setPushType(String pushType) {
		this.pushType = pushType;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public int getiOSBadge() {
		return iOSBadge;
	}
	public void setiOSBadge(int iOSBadge) {
		this.iOSBadge = iOSBadge;
	}
	public String getAndroidNotifyType() {
		return androidNotifyType;
	}
	public void setAndroidNotifyType(String androidNotifyType) {
		this.androidNotifyType = androidNotifyType;
	}
	public String getAndroidOpenType() {
		return androidOpenType;
	}
	public void setAndroidOpenType(String androidOpenType) {
		this.androidOpenType = androidOpenType;
	}
	public String getAndroidOpenUrl() {
		return androidOpenUrl;
	}
	public void setAndroidOpenUrl(String androidOpenUrl) {
		this.androidOpenUrl = androidOpenUrl;
	}
	public String getAndroidActivity() {
		return androidActivity;
	}
	public void setAndroidActivity(String androidActivity) {
		this.androidActivity = androidActivity;
	}
	
}
