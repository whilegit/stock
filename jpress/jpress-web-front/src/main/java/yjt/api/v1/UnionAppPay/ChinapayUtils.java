package yjt.api.v1.UnionAppPay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.jfinal.kit.PathKit;

import chinapay.Base64;
import chinapay.SecureLink;
import yjt.Utils;

public class ChinapayUtils {

	public static String merId = "808080211306315";
	
	public static String test(){
		chinapay.PrivateKey key=new chinapay.PrivateKey(); 
		boolean flag1; 
		String msg, priKeyPath;
		priKeyPath = PathKit.getRootClassPath()+ "/certs/MerPrK_808080211306315_20170927140135.key";
		String merDate = "20171007";
		String merSeqId = String.format("%08d", (int)(Math.random() * 99999999));
		String cardNo = "6228480362349687719"; // //6236681480007046323
		String usrName = "林忠仁";//朱千增
		String openBank = "农业银行"; //建设银行
		String prov = "浙江省"; //
		String city = "温岭市"; //台州市
		String transAmt = "100";
		String purpose = "test";
		String subBank = ""; //玉环支行
		
		
		String flag = "00";
		String version = "20160530";
		String termType = "08";
		String payMode = "1";
		
		/*
		 * merId + merDate + merSeqId + cardNo + usrName+ openBank + 
		 * prov + city + transAmt + purpose + subBank + flag + version + 
		 * termType+payMode+userId+userRegisterTime+userMail+
		 * userMobile+diskSn+mac+imei+ip+coordinates+
		 * baseStationSn+codeInputType+mobileForBank+desc
		 * 
		 * String strParams = "merId=" + merId + "&merDate=" + merDate + "&merSeqId=" + merSeqId+ "&cardNo=" + cardNo+ "&usrName=" + usrName
				 + "&openBank=" + openBank
				 + "&prov=" + prov+ "&city=" + city+ "&transAmt=" + transAmt + "&purpose=" + purpose
				 + "&subBank=" + subBank+ "&flag=" + flag+ "&version=" + version+ "&signFlag=1&termType=08&payMode=1&chkValue=" + chkValue;
		 */
		
		try {
			usrName = URLEncoder.encode(usrName, "UTF-8");
			openBank = URLEncoder.encode(openBank, "UTF-8");
			prov = URLEncoder.encode(prov, "UTF-8");
			city = URLEncoder.encode(city, "UTF-8");
			purpose = URLEncoder.encode(purpose, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 将要签名的数据传给msg
		msg = merId+ merDate + merSeqId +cardNo +usrName+openBank 
					 +prov +city +transAmt+purpose+subBank+flag+version + termType +payMode;		
		System.out.println("要签名的数据:"+ msg);
			
        //对签名的数据进行Base64编码
        String msg1 = new String(Base64.encode(msg.toString().getBytes()));
        System.out.println("要签名的数据进行Base64编码后为:"+ msg1);
        flag1=key.buildKey(merId, 0, priKeyPath); 
		if (flag1==false) { 
			System.out.println("build key error!"); 
			return ""; 
		}
		//签名
        SecureLink s = new SecureLink(key);
		String chkValue = s.Sign(msg1);
		System.out.println("signFlag=1 时签名内容:"+ chkValue);
		
		
		String url = "http://sfj.chinapay.com/dac/SinPayServletUTF8";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		 params.add(new BasicNameValuePair("merId", merId));
		 params.add(new BasicNameValuePair("merDate", merDate));
		 params.add(new BasicNameValuePair("merSeqId",merSeqId));
		 params.add(new BasicNameValuePair("cardNo",cardNo));
		 params.add(new BasicNameValuePair("usrName",usrName));
		 params.add(new BasicNameValuePair("openBank",openBank));
		 params.add(new BasicNameValuePair("prov", prov));
		 params.add(new BasicNameValuePair("city", city));
		 params.add(new BasicNameValuePair("transAmt", transAmt));
		 params.add(new BasicNameValuePair("purpose",purpose));
		 params.add(new BasicNameValuePair("subBank", subBank));
		 params.add(new BasicNameValuePair("flag", flag));
		 params.add(new BasicNameValuePair("version", version));
		 params.add(new BasicNameValuePair("signFlag", "1"));
		 params.add(new BasicNameValuePair("termType",termType));
		 params.add(new BasicNameValuePair("payMode", payMode));
		 params.add(new BasicNameValuePair("chkValue", chkValue));
		 /*
		 String strParams = "merId=" + merId + "&merDate=" + merDate + "&merSeqId=" + merSeqId+ "&cardNo=" + cardNo+ "&usrName=" + usrName
				 + "&openBank=" + openBank
				 + "&prov=" + prov+ "&city=" + city+ "&transAmt=" + transAmt + "&purpose=" + purpose
				 + "&subBank=" + subBank+ "&flag=" + flag+ "&version=" + version+ "&signFlag=1&termType=08&payMode=1&chkValue=" + chkValue;
		 //String str = "";
		 
		 try {
			str = URLEncoder.encode(strParams, "UTF-8");
			str = str.replaceAll("%3D", "=");
			str = str.replaceAll("%26", "&");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		 
		 String result = "";
		try {
			//System.out.println(strParams);
			result = Utils.post(url, params, null);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		/*
		 * msg = merId+ merDate + merSeqId +cardNo +usrName+openBank 
					 +prov +city +transAmt+purpose+subBank+flag+version + termType +payMode;		
		 
		String html =  "<form name=\"oraform\" action=\"http://sfj.chinapay.com/dac/SinPayServletUTF8\" method=\"post\">" + 
							"<input type=\"text\" name=\"merId\" value=\""+merId+"\">" + 
							"<input type=\"text\" name=\"merDate\" value=\""+merDate+"\">" + 
							"<input type=\"text\" name=\"merSeqId\" value=\""+merSeqId+"\">" + 
							"<input type=\"text\" name=\"cardNo\" value=\""+cardNo+"\">" + 
							"<input type=\"text\" name=\"usrName\" value=\""+usrName+"\">" + 
							"<input type=\"text\" name=\"openBank\" value=\""+openBank+"\">" + 
							"<input type=\"text\" name=\"prov\" value=\""+prov+"\">" + 
							"<input type=\"text\" name=\"city\" value=\""+city+"\">" + 
							"<input type=\"text\" name=\"transAmt\" value=\""+transAmt+"\">" + 
							"<input type=\"text\" name=\"purpose\" value=\""+purpose +"\">" + 
							//"<input type=\"text\" name=\"subBank\" value=\""+subBank +"\">" + 
							"<input type=\"text\" name=\"flag\" value=\""+flag +"\">" + 
							"<input type=\"text\" name=\"version\" value=\""+version +"\">" + 
							"<input type=\"text\" name=\"signFlag\" value=\"1\">" + 
							"<input type=\"text\" name=\"termType\" value=\""+termType+"\">" + 
							"<input type=\"text\" name=\"payMode\" value=\""+payMode+"\">" + 
							//"<input type=\"hidden\" name=\"userId\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"userRegisterTime\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"userMail\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"userMobile\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"diskSn\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"mac\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"imei\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"ip\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"coordinates\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"baseStationSn\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"codeInputType\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"mobileForBank\" value=\"\">" + 
							//"<input type=\"hidden\" name=\"desc\" value=\"\">" + 
							"<input type=\"text\" name=\"chkValue\" value=\""+chkValue+"\">" + 
							"<input type=\"submit\" name=\"sub\" value=\"提交\">" + 
						"</form>";
		
		return "<html><head><meta charset=\"utf-8\"></head><body>"+html+"</body></html>";
		*/
		
		
	}
}
