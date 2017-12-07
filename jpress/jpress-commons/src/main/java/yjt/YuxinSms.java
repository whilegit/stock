package yjt;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;


public class YuxinSms {
	protected static final Log log = Log.getLog(YuxinSms.class);

	private static final String URL_BASE = "http://api.sms.cn/sms/?ac=send";
	private String uid = "ejie666";
	private String password = "z13005000";
	
	private String mobile;
	private String content;
	private String template_id;
	public YuxinSms(String mobile, String content, String template_id){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			password = password + uid;
			md.update(password.getBytes());
			password = new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.mobile = mobile;
		try {
			this.content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.template_id = template_id;
	}

	public boolean send(){
		String url = genSmsUrl();
		try {
			String result = Utils.get(url, null, null);
			
			log.info("已发放短信，返回值："+new String(result.getBytes("ISO8859-1"), "UTF-8")+" " + url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.info("短信发送失败, 错误："+e.getMessage()+"。链接：" + url);

			return false;
		}
		return true;
	}
	
	private String genSmsUrl(){
		String ret = null;
		ret = URL_BASE + "&uid="+ uid + "&pwd=" + password + "&mobile=" + this.mobile + "&content=" + this.content;
		if(StrKit.notBlank(template_id)){
			ret += "&template=" + template_id;
		}
		return ret;
	}
}
