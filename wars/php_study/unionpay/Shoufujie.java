package unionpay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.bouncycastle.util.encoders.Base64;

import com.shifangju.freemarker_study.App;

import chinapay.SecureLink;

/*
 {"error_code":0,"reason":"Succes","result":{"bankname":"浙江省农村信用社联合社","banknum":"14293300","cardprefixnum":"623091","cardname":"借记IC卡","cardtype":"银联借记卡","cardprefixlength":"6","cardlength":"19","isLuhn":true,"iscreditcard":1,"bankurl":"","enbankname":"","abbreviation":"ZJNX","bankimage":"http:\/\/auth.apis.la\/bank\/114_ZJNX.png","servicephone":""},"ordersign":"20170928173238798554575351"}
 */
public class Shoufujie {
	public static String merId = "808080211306315";
	
	
	public static void init() throws UnsupportedEncodingException{
		//初始化key文件： 
		chinapay.PrivateKey key=new chinapay.PrivateKey(); 
		boolean flag1; 
		String msg = "", priKeyPath = "D:\\certs\\MerPrK_808080211306315_20170927140135.key";
		/*
		 * merId + merDate + merSeqId + cardNo + usrName+ openBank + prov + city + transAmt + purpose + subBank + flag + version + termType+payMode+userId+userRegisterTime+userMail+userMobile+diskSn+mac+imei+ip+coordinates+baseStationSn+codeInputType+mobileForBank+desc
		 * 
		 */
		 List<NameValuePair> params = new ArrayList<NameValuePair>();
		 params.add(new BasicNameValuePair("merId", "808080211306315"));
		 params.add(new BasicNameValuePair("merDate", "20170930"));
		 params.add(new BasicNameValuePair("merSeqId","0000001"));
		 params.add(new BasicNameValuePair("cardNo","6236681480007046323"));
		 params.add(new BasicNameValuePair("usrName","朱千增"));
		 params.add(new BasicNameValuePair("openBank","建设银行"));
		 params.add(new BasicNameValuePair("prov", "浙江省"));
		 params.add(new BasicNameValuePair("city", "台州市"));
		 params.add(new BasicNameValuePair("transAmt", "10"));
		 params.add(new BasicNameValuePair("purpose","test"));
		 params.add(new BasicNameValuePair("subBank", "玉环支行"));
		 params.add(new BasicNameValuePair("flag", "00"));
		 params.add(new BasicNameValuePair("version", "20160530"));
		 params.add(new BasicNameValuePair("signFlag", "1"));
		 params.add(new BasicNameValuePair("termType","08"));
		 params.add(new BasicNameValuePair("payMode", "1"));
		 
		 //808080290000001
		 //00000000
		 //10273765
		 //000000000001156
		 //20100806
		 //0001
		 String[] signField = {"merId", "merDate", "merSeqId", "cardNo", "usrName", "openBank", "prov", "city", 
				 			   "transAmt", "purpose", "subBank", "flag", "version", "termType", "payMode", "userId", 
				 			   "userRegisterTime", "userMail", "userMobile", 
				 			   "diskSn", "mac", "imei", "ip", "coordinates", "baseStationSn", "codeInputType", "mobileForBank", "desc"};
		 for(String field : signField){
			 for(NameValuePair nvp : params){
				 if(field.equals(nvp.getName())){
					 msg += nvp.getValue();
					 break;
				 }
			 }
		 }
		 //msg = URLEncoder.encode(msg, "UTF-8");
		 //System.out.println(URLEncoder.encode(msg, "utf-8"));
		 System.out.println(msg);
		
		//对签名的数据进行Base64编码
		String msg1 = new String(Base64.encode(msg.getBytes()));
		System.out.println("要签名的数据进行Base64编码后为:"+ msg1);
		flag1=key.buildKey(merId, 0, priKeyPath); 
		if (flag1==false) { 
			System.out.println("build key error!"); 
			return; 
		}
		
		//签名
		SecureLink s = new SecureLink(key);
		String chkValue = s.Sign(msg1);
		params.add(new BasicNameValuePair("chkValue", chkValue));
		
		System.out.println("signFlag=1 时签名内容:"+ chkValue);
		
		try {
			String url = "http://sfj-test.chinapay.com/dac/SinPayServletUTF8";
			String result  = App.post(url, params, null);
			System.out.println(result);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
