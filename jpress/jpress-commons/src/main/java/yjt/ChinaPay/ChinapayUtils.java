package yjt.ChinaPay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

import chinapay.Base64;
import chinapay.SecureLink;
import yjt.Utils;

public class ChinapayUtils {
	protected static final Log log = Log.getLog(ChinapayUtils.class);

	public static String merId; // = "808080211306315";
	
	public static void init() {
		PropKit.use("jpress.properties");
		merId = PropKit.get("chinapay_merId", "");
		if(StrKit.isBlank(merId)) {
			log.error("初始化时无法获取chinapay_merId参数，请检查jpress.properties参数");
		}
	}
	
	public static ChinapayTransBean getTransBean(String cardNo, String usrName, String openBank, String prov, String city,
												 int transAmt, String purpose) {
		if(!StrKit.notBlank(cardNo, usrName, openBank, prov, city) || transAmt <= 0) {
			log.error("参数为空或金额不大于0(purpose可以为空)：" + "cardNo:" + cardNo + ",usrName=" + usrName + ",openBank=" + openBank + ",prov=" + prov + ",city=" + city + ",transAmt=" + transAmt + ",purpose=" + purpose);
			return null;
		}
		ChinapayTransBean bean = new ChinapayTransBean();
		bean.setMerId(merId);
		bean.setMerDate(Utils.getDayNumber_Ymd(new Date()));
		bean.setMerSeqId(String.format("%08d", (int)(Math.random() * 99999999)));
		bean.setCardNo(cardNo);
		bean.setUsrName(usrName);
		bean.setOpenBank(openBank);
		bean.setProv(prov);
		bean.setCity(city);
		bean.setTransAmt(""+transAmt);   //单位分
		bean.setPurpose(purpose != null ? purpose : "");
		
		bean.setFlag("00"); 
		bean.setVersion("20160530");
		bean.setTermType("08");
		bean.setPayMode("1");
		return bean;
	}
	
	public static class PayStatus{
		

		public String responseCode;		
		public HashMap<String, String> pairs = new HashMap<String, String>();
		public String raw;
		
		public String tips;
		public boolean success = false;
		/*
		 * 0000	接收成功	提交成功
		   0100	接收失败	商户提交的字段长度、格式错误
		   0101	接收失败  商户验签错误
		   0102	接收失败	手续费计算出错
		   0103	接收失败	商户备付金帐户金额不足
		   0104	接收失败	操作拒绝
		   0105	待查询	重复交易
		 */
		private static String mapTips(String responseCode) {
			String tips = "";
			if(StrKit.isBlank(responseCode)) {
				tips = "状态码为空";
			}else if("0000".equals(responseCode)) {
				tips = "提交成功";
			}else if("0100".equals(responseCode)) {
				tips = "接收失败(商户提交的字段长度、格式错误)";
			}else if("0101".equals(responseCode)) {
				tips = "接收失败 (商户验签错误)";
			}else if("0102".equals(responseCode)) {
				tips = "接收失败(手续费计算出错)";
			}else if("0103".equals(responseCode)) {
				tips = "接收失败(商户备付金帐户金额不足)";
			}else if("0104".equals(responseCode)) {
				tips = "接收失败(操作拒绝)";
			}else if("0105".equals(responseCode)) {
				tips = "待查询(重复交易)";
			}else {
				tips = "不可识别";
			}
			return tips;
		}
		
		/*
		 * responseCode=0000&merId=808080211306315&merDate=20171027&merSeqId=80546581&cpDate=20171027&cpSeqId=276644&transAmt=1000&stat=s&cardNo=6228581099025464660&chkValue=5CDBA0F9B97E3C00AC7115002EA1240BCC3E66E373E9AB4BEE3E95E592F141810043C5EDC29A73B842257B4110159F699385FDAB722EED59DE32DC8B74CC999A1D818F0489B99178383F6AD4435B74D615277B9A8CC9EDA921EF913DCC8CB48C755821985403575B47B4E45B2026C8E8F22382B248A236890E1B8FBA5ACB32E5
		 */
		public static PayStatus parseResult(String result) {
			if(StrKit.isBlank(result)) return null;
			PayStatus st = new PayStatus();
			st.raw = result;
			String[] items = result.split("&");
			for(String item : items) {
				String[] kv = item.split("=");
				if(kv.length == 2) {
					st.pairs.put(kv[0], kv[1]);
					if("responseCode".equals(kv[0])) {
						st.responseCode = kv[1];
						if("0000".equals(kv[1])) {
							st.success = true;
						}
						st.tips = mapTips(kv[1]);
					}
				}
			}
			return st;
		}
		
		public String getTips() {
			return tips;
		}

		public void setTips(String tips) {
			this.tips = tips;
		}

		public String getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(String responseCode) {
			this.responseCode = responseCode;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}
	}
	
	public static PayStatus pay(ChinapayTransBean bean) {
		String merDate = bean.getMerDate();
		String merSeqId = bean.getMerSeqId();
		String cardNo = bean.getCardNo();
		String usrName = bean.getUsrName();
		String openBank = bean.getOpenBank();
		String prov = bean.getProv();
		String city = bean.getCity();
		String transAmt = bean.getTransAmt();
		String purpose = bean.getPurpose();
		String subBank = "";
		
		String flag = bean.getFlag();//固定 "00";
		String version = bean.getVersion(); //固定 "20160530";
		String termType = bean.getTermType(); //固定 "08";
		String payMode = bean.getPayMode(); //固定 "1";
		
		// 将要签名的数据传给msg
		String msg, priKeyPath;
		priKeyPath = PathKit.getRootClassPath()+ "/certs/MerPrK_808080211306315_20170927140135.key";
		msg = merId+ merDate + merSeqId +cardNo +usrName+openBank 
					 +prov +city +transAmt+purpose+subBank+flag+version + termType +payMode;
			
        //对签名的数据进行Base64编码
        String msg1 = null;
		try {
			char[] chs = Base64.encode(msg.toString().getBytes("GBK"));
			msg1 = new String(chs);
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        chinapay.PrivateKey key=new chinapay.PrivateKey(); 
        boolean flag1=key.buildKey(merId, 0, priKeyPath); 
		if (flag1==false) { 
			//System.out.println("build key error!"); 
			log.error("对数据签名时失败，" +  priKeyPath + "\r\n" + msg);
			return null; 
		}
		//签名
		
        SecureLink s = new SecureLink(key);
		String chkValue = s.Sign(msg1);
		
		String strParams = "merId=" + merId + "&merDate=" + merDate + "&merSeqId=" + merSeqId+ "&cardNo=" + cardNo+ "&usrName=" + usrName
				 + "&openBank=" + openBank
				 + "&prov=" + prov+ "&city=" + city+ "&transAmt=" + transAmt + "&purpose=" + purpose
				 + "&flag=" + flag+ "&version=" + version+ "&signFlag=1&termType=08&payMode=1&chkValue=" + chkValue;
		String str = "";
		
		try {
			str = URLEncoder.encode(strParams, "UTF-8");
			str = str.replaceAll("%3D", "=");
			str = str.replaceAll("%26", "&");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String result = "";
		try {
			String url = "http://sfj.chinapay.com/dac/SinPayServletUTF8";
			result = post(url, str, null);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info(result);
		
		return PayStatus.parseResult( result);
	}
	
	public static String test(){
		chinapay.PrivateKey key=new chinapay.PrivateKey(); 
		boolean flag1; 
		String msg, priKeyPath;
		priKeyPath = PathKit.getRootClassPath()+ "/certs/MerPrK_808080211306315_20170927140135.key";
		String merDate = "20171009";
		String merSeqId = String.format("%08d", (int)(Math.random() * 99999999));
		String cardNo = "6236681480007046323"; //6228480362349687719
		String usrName = "朱千增";    //林忠仁
		String openBank = "建设银行"; //农业银行
		String prov = "浙江省";
		String city = "台州市"; //温岭市
		String transAmt = "100";
		String purpose = "test";
		String subBank = ""; //玉环支行
		
		String flag = "00";
		String version = "20160530";
		String termType = "08";
		String payMode = "1";
		
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
		
		String strParams = "merId=" + merId + "&merDate=" + merDate + "&merSeqId=" + merSeqId+ "&cardNo=" + cardNo+ "&usrName=" + usrName
				 + "&openBank=" + openBank
				 + "&prov=" + prov+ "&city=" + city+ "&transAmt=" + transAmt + "&purpose=" + purpose
				 + "&flag=" + flag+ "&version=" + version+ "&signFlag=1&termType=08&payMode=1&chkValue=" + chkValue;
		String str = "";
		
		try {
			str = URLEncoder.encode(strParams, "UTF-8");
			str = str.replaceAll("%3D", "=");
			str = str.replaceAll("%26", "&");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String result = "";
		try {
			String url = "http://sfj.chinapay.com/dac/SinPayServletUTF8";
			result = post(url, str, null);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static String post(String url, String params, List<NameValuePair> headers) throws ClientProtocolException, IOException {
		String result = null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		if(headers != null && headers.size() > 0) {
			Iterator<NameValuePair> it = headers.iterator();
			while(it.hasNext()) {
				NameValuePair entry = it.next();
				httpPost.setHeader(entry.getName(), entry.getValue());
			}
		}
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded"); //Content-Type:
		httpPost.setEntity(new StringEntity(params));
		Header[] headers1 = httpPost.getAllHeaders();
		CloseableHttpResponse response = httpclient.execute(httpPost);

		try {
			StatusLine status = response.getStatusLine();
			int code = status.getStatusCode();
			HttpEntity entity = response.getEntity();
			
			if(code == 200){
				if(entity != null){
					Charset charset = Utils.getContentTypeCharset(response);
					if(charset == null) charset = Charset.forName("UTF-8");
					result = IOUtils.toString(entity.getContent(), charset.name());
				}
			} else if(entity != null) {
				EntityUtils.consume(entity);
			}
		} finally {
			response.close();
		}
		return result;
	}
}
