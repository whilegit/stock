package yjt.verify;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.StrKit;

import yjt.Utils;

public class BankcardDetail {

	protected String html;
	protected int error_code;
	protected String reason;
	protected Result result;
	
	public static BankcardDetail verify(String bankcard){
		if(!StrKit.notBlank(bankcard)){
			return null;
		}
		BankcardDetail detail = null;
		String url = "http://api43.market.alicloudapi.com/api/c43";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("bankcard", bankcard));

		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Authorization", "APPCODE " + AppCodes.BANKCARDDETAIL_APPCODE));
		headers.add(new BasicNameValuePair("Content-Type", "application/json; charset=utf-8"));
		headers.add(new BasicNameValuePair("X-Ca-Request-Mode", "debug"));
			
		try {
			String html = Utils.get(url, params, headers);
			if(StrKit.notBlank(html)){
				BankcardDetail verifier = JSON.parseObject(html, BankcardDetail.class);
				if(verifier != null && verifier.getErrorCode() == 0){
					detail = verifier;
					detail.setHtml(html);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return detail;
	}
	
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
	
	public int getErrorCode() {
		return error_code;
	}

	public void setStatus(int errorCode) {
		this.error_code = errorCode;
	}

	public String getReason() {
		return reason;
	}

	public void setMsg(String reason) {
		this.reason = reason;
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
		return "{'error_code':'" + error_code + "','reason':'" + reason + "','result':" + result.toString() + "}";
	}

	public class Result{
		
		/*
		"bankname": "中国银行",
	    "banknum": "1040000",
	    "cardprefixnum": "621569",
	    "cardname": "长城福农借记卡普卡",
	    "cardtype": "银联借记卡",
	    "cardprefixlength": "6",
	    "cardlength": "19",
	    "isLuhn": true,
	    "bankurl": "http://www.boc.cn",
	    "enbankname": "Bank of China",
	    "abbreviation": "BOC",
	    "bankimage": "http://auth.apis.la/bank/cb.png",
	    "servicephone": "95566",
	    "province": "湖北省",
	    "city": "武汉"
	    */
		protected String bankname;
		protected String banknum;
		protected String cardprefixnum;
		protected String cardname;
		protected String cardtype;
		protected String cardprefixlength;
		protected String cardlength;
		protected boolean isLuhn;
		protected String bankurl;
		protected String enbankname;
		protected String abbreviation;
		protected String bankimage;
		protected String servicephone;
		protected String province;
		protected String city;

		public String getBankname() {
			return bankname;
		}

		public void setBankname(String bankname) {
			this.bankname = bankname;
		}

		public String getBanknum() {
			return banknum;
		}

		public void setBanknum(String banknum) {
			this.banknum = banknum;
		}

		public String getCardprefixnum() {
			return cardprefixnum;
		}

		public void setCardprefixnum(String cardprefixnum) {
			this.cardprefixnum = cardprefixnum;
		}

		public String getCardname() {
			return cardname;
		}

		public void setCardname(String cardname) {
			this.cardname = cardname;
		}

		public String getCardtype() {
			return cardtype;
		}

		public void setCardtype(String cardtype) {
			this.cardtype = cardtype;
		}

		public String getCardprefixlength() {
			return cardprefixlength;
		}

		public void setCardprefixlength(String cardprefixlength) {
			this.cardprefixlength = cardprefixlength;
		}

		public String getCardlength() {
			return cardlength;
		}

		public void setCardlength(String cardlength) {
			this.cardlength = cardlength;
		}

		public boolean isLuhn() {
			return isLuhn;
		}

		public void setLuhn(boolean isLuhn) {
			this.isLuhn = isLuhn;
		}

		public String getBankurl() {
			return bankurl;
		}

		public void setBankurl(String bankurl) {
			this.bankurl = bankurl;
		}

		public String getEnbankname() {
			return enbankname;
		}

		public void setEnbankname(String enbankname) {
			this.enbankname = enbankname;
		}

		public String getAbbreviation() {
			return abbreviation;
		}

		public void setAbbreviation(String abbreviation) {
			this.abbreviation = abbreviation;
		}

		public String getBankimage() {
			return bankimage;
		}

		public void setBankimage(String bankimage) {
			this.bankimage = bankimage;
		}

		public String getServicephone() {
			return servicephone;
		}

		public void setServicephone(String servicephone) {
			this.servicephone = servicephone;
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

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "{'bankname':'"+bankname+"','banknum':'" + banknum + "','cardprefixnum':'" + cardprefixnum + "','cardname':'" + cardname + "'," + 
					"'cardtype':'" + cardtype + "','cardprefixlength':'" + cardprefixlength + "','cardlength':'" + cardlength +
					"','isLuhn':'" + isLuhn + "','bankurl':'" + bankurl
					+ "','enbankname':'" + enbankname+ "','abbreviation':'" + abbreviation +"','bankimage':'" + bankimage 
					+"','servicephone':'" + servicephone +"','province':'" + province +"','city':'" + city+"'}";
		}
	}
}
