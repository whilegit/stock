package yjt.verify;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.StrKit;

import yjt.Utils;

public class MobileVerify {

	protected int status;
	protected String msg;
	protected Result result;
	
	public static boolean verify(String idcard, String mobile, String realname, String typeid){
		if(!StrKit.notBlank(idcard, mobile, realname, typeid)){
			return false;
		}
		boolean checked = false;
		String url = "http://jisusjhsm.market.alicloudapi.com/mobileverify/verify";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("idcard", idcard));
		params.add(new BasicNameValuePair("mobile", mobile));
		params.add(new BasicNameValuePair("realname", realname));
		if(StrKit.isBlank(typeid)){
			typeid = "1";
		}
		params.add(new BasicNameValuePair("typeid", typeid));

		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Authorization", "APPCODE " + AppCodes.MOBILEVERIFY_APPCODE));
		headers.add(new BasicNameValuePair("Content-Type", "application/json; charset=utf-8"));
		headers.add(new BasicNameValuePair("X-Ca-Request-Mode", "debug"));
		
		try {
			String html = Utils.get(url, params, headers);
			System.out.println(html);
			if(StrKit.notBlank(html)){
				MobileVerify verifier = JSON.parseObject(html, MobileVerify.class);
				if(verifier != null && verifier.getStatus() == 0){
					Result result = verifier.getResult();
					if(result != null && mobile.equals(result.getMobile()) && 
							idcard.equals(result.getIdcard()) && result.getVerifystatus() == 0){
						System.out.println(verifier.toString());
						checked = true;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return checked;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{'status':'" + status + "','msg':'" + msg + "','result':" + result.toString() + "}";
	}

	class Result{
		protected String mobile;
		protected String realname;
		protected String idcard;
		protected int verifystatus;
		protected String verifymsg;
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getRealname() {
			return realname;
		}
		public void setRealname(String realname) {
			this.realname = realname;
		}
		public String getIdcard() {
			return idcard;
		}
		public void setIdcard(String idcard) {
			this.idcard = idcard;
		}
		public int getVerifystatus() {
			return verifystatus;
		}
		public void setVerifystatus(int verifystatus) {
			this.verifystatus = verifystatus;
		}
		public String getVerifymsg() {
			return verifymsg;
		}
		public void setVerifymsg(String verifymsg) {
			this.verifymsg = verifymsg;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "{'mobile':'" + mobile + "','realname':'" + realname + "','idcard':'" + idcard + "'," + 
					"'verifystatus':'" + verifystatus + "','verifymsg':'" + verifymsg + "'}";
		}
	}
}
