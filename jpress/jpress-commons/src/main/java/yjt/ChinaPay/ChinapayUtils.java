package yjt.ChinaPay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
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
	
	public static ChinapayTransBean getTransBean() {
		return null;
		
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
