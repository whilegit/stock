package yjt.verify;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.StrKit;

import yjt.Utils;

public class IdcardVerify {
	
	protected int status;
	protected String msg;
	protected Result result;
	
	public static boolean verify(String realname, String idcard){
		if(StrKit.isBlank(realname) || StrKit.isBlank(idcard)){
			return false;
		}
		boolean ret = false;
		String url = "http://jisusfzsm.market.alicloudapi.com/idcardverify/verify";
		//String url = "http://phpstudy/test2.php";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("idcard", idcard));
		params.add(new BasicNameValuePair("realname", realname));

		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Authorization", "APPCODE " + AppCodes.IDCARDVERIFY_APPCODE));
		headers.add(new BasicNameValuePair("Content-Type", "application/json; charset=utf-8"));
		headers.add(new BasicNameValuePair("X-Ca-Request-Mode", "debug"));
		
		try {
			String result = Utils.get(url, params, headers);
			if(!StrKit.isBlank(result)){
				IdcardVerify verifier = JSON.parseObject(result, IdcardVerify.class);
				ret = (verifier != null && verifier.getStatus() == 0 && verifier.getMsg().equals("ok") 
						&& verifier.getResult().getIdcard().equals(idcard));
				System.out.println(verifier.toString());
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return ret;
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

	public String toString(){
		return "{'status':'" + status + "','msg':'" + msg +"',result:" + result.toString() + "}";
	}
	class Result{
		String idcard;
		String realname;
		String province;
		String city;
		String town;
		String sex;
		String birth;
		int verifystatus;
		String verifymsg;
		
		public String getIdcard() {
			return idcard;
		}
		public void setIdcard(String idcard) {
			this.idcard = idcard;
		}
		public String getRealname() {
			return realname;
		}
		public void setRealname(String realname) {
			this.realname = realname;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getTown() {
			return town;
		}
		public void setTown(String town) {
			this.town = town;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getBirth() {
			return birth;
		}
		public void setBirth(String birth) {
			this.birth = birth;
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
		
		public String toString(){
			return "{'idcard': '" + idcard +
			"','realname': '" + realname + 
			"','province': '" + province + 
			"','city': '" + city + 
			"','town': '" + town + 
			"','sex': '" + sex + 
			"','birth': '" + birth + 
			"','verifystatus': '" + verifystatus + 
			"','verifymsg': '" + verifymsg + "'}"; 
		}

	}
	
}
