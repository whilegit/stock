package yjt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;

public class Utils {
	protected static final SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy-MM-dd");
	protected static final SimpleDateFormat sdfYm_d = new SimpleDateFormat("yyyyMM/dd");
	protected static final SimpleDateFormat sdfYmdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected static final SimpleDateFormat sdfNumber = new SimpleDateFormat("yyyyMMddHHmmss");
	protected static final Pattern MOBILE_PATTERN = Pattern.compile("^1[345789][0-9]{9}$");
	
	protected static String domain = null;
	
	public static long getTodayStartTime(){
		Date d = new Date();
		String t = sdfYmd.format(d) + " 00:00:00";
		try {
			d = sdfYmdHms.parse(t);
		} catch (ParseException e) {
		}
		return d.getTime();
	}
	
	public static long getDayStartTime(Date d) {
		String t = sdfYmd.format(d) + " 00:00:00";
		try {
			d = sdfYmdHms.parse(t);
		} catch (ParseException e) {
		}
		return d.getTime();
	}
	
	public static Date getYmd(String s){
		Date d = null;
		try{
			d = sdfYmd.parse(s);
		} catch (Exception e){
			//
		}
		return d;
	}
	
	/**
	 * 拆分以逗号隔开的BigInteger组成的字符串
	 * @param source
	 * @param delimiter
	 * @param filter 是否过滤相同的值
	 * @return
	 */
	public static ArrayList<BigInteger> splitToBigInteger(String source, String delimiter, boolean filter){
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		if(StrKit.notBlank(source, delimiter)){
			String[] ary = source.split(delimiter);
			if(ary != null && ary.length > 0){
				for(String s : ary){
					long l = Long.parseLong(s);
					BigInteger bi = BigInteger.valueOf(l);
					if(filter && ret.contains(bi)){
						continue;
					} else {
						ret.add(bi);
					}
				}
			}
		}
		return ret;
	}
	
	
	public static boolean isMobile(String mobile){
		boolean ret = false;
		if(StrKit.notBlank(mobile)){
			Matcher m = MOBILE_PATTERN.matcher(mobile);
			ret = m.matches();
		}
		return ret;
	}
	
	protected static final DecimalFormat DF0_00   =new DecimalFormat("#0.00");
	public static String bigDecimalRound2(BigDecimal bd){
		return DF0_00.format(bd);
	}
	
	public static String toYmd(Date d) {
		if(d == null) return "";
		return sdfYmd.format(d);
	}
	
	public static String[] toYmdAry(Date d) {
		return  sdfYmd.format(d).split("[\\/\\-]");
	}
	
	
	
	public static String toYm_d(Date d) {
		if(d == null) return "";
		return sdfYm_d.format(d);
	}
	
	public static String toYmdHms(Date d) {
		if(d == null) return "";
		return sdfYmdHms.format(d);
	}
	
	public static String getDayNumber(Date d) {
		if(d == null) return "";
		return sdfNumber.format(d);
	}
	
	public static int random(int min, int max) {
		return (int) (Math.random() * (max -min) + min);
	}
	
	public static int days(Date fromDate, Date toDate) {
		long internal = (getDayStartTime(toDate) - getDayStartTime(fromDate)) / 1000;
		return (int) (internal / 86400);
	}
	
	public static Date getNextMonthDay() {
		 Calendar cal = Calendar.getInstance();
	     cal.add(Calendar.MONTH, 1);
	     Date d = cal.getTime();
	     long stamp = getDayStartTime(d);
	     return new Date(stamp);
	}
	
	public static Date getPrevMonthDay(Date td) {
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(td);
	     cal.add(Calendar.MONTH, -1);
	     Date d = cal.getTime();
	     long stamp = getDayStartTime(d);
	     return new Date(stamp);
	}
	
	public static String getFileExtention(String path){
		int len = path.length();
		int last_slash = path.lastIndexOf('/');
		if(last_slash == -1 || last_slash >= len -1){
			return "";
		}
		String filename = path.substring(last_slash + 1, len);
		int lash_dot = filename.lastIndexOf('.');
		if(lash_dot == -1 || lash_dot >= len -1){
			return "";
		}
		
		return filename.substring(lash_dot+1, filename.length());
	}
	
	public static void exec(String[] commandStr) {  
        BufferedReader br = null;  
        try {  
            Process p = Runtime.getRuntime().exec(commandStr);  

            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;  
            StringBuilder sb = new StringBuilder();  
            while ((line = br.readLine()) != null) {  
                sb.append(line + "\n");  
            }  
            //p.destroy();
            System.out.println(sb.toString());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }   
        finally  
        {  
            if (br != null)  
            {  
                try {  
                    br.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
	
	/**
	 * HTTP Post方法加载页面
	 * @param url
	 * @param params          键值对
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, List<NameValuePair> params, List<NameValuePair> headers) throws ClientProtocolException, IOException {
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
		httpPost.setEntity(new UrlEncodedFormEntity(params));
		CloseableHttpResponse response = httpclient.execute(httpPost);

		try {
			StatusLine status = response.getStatusLine();
			int code = status.getStatusCode();
			HttpEntity entity = response.getEntity();
			
			if(code == 200){
				if(entity != null){
					Charset charset = getContentTypeCharset(response);
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
	

	
	/**
	 * HTTP Get方法加载页面
	 * @param url
	 * @param defaultCharset         自动判断编码类型 > defaultCharset > UTF-8, 兜底为UTF-8
	 * @return 返回Http报文的Body部分
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String get(String url,  List<NameValuePair> params, List<NameValuePair> headers) throws ClientProtocolException, IOException {
		String result = null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		if(params != null && params.size() > 0){
			String query = "";
			Iterator<NameValuePair> it = params.iterator();
			while(it.hasNext()){
				NameValuePair nvp = it.next();
				query += "&" + URLEncoder.encode(nvp.getName(), "UTF-8") + "=" + URLEncoder.encode(nvp.getValue(), "UTF-8");
			}
			
			if(url.indexOf('?') < 0){
				query = query.substring(1, query.length());
				url = url + "?" + query;
			} else {
				url = url + query;
			}
		}
		System.out.println(url);
		HttpGet httpGet = new HttpGet(url);
		
		if(headers != null && headers.size() > 0) {
			Iterator<NameValuePair> it = headers.iterator();
			while(it.hasNext()) {
				NameValuePair entry = it.next();
				httpGet.setHeader(entry.getName(), entry.getValue());
			}
		}
		
		CloseableHttpResponse response = httpclient.execute(httpGet);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager. 
		try {
			StatusLine status = response.getStatusLine();
			int code = status.getStatusCode();
			HttpEntity entity = response.getEntity();
			if(code == 200){
				if(entity != null){
					//long len = entity.getContentLength();
					Charset charset = getContentTypeCharset(response);
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
	
	/**
	 * 用于getContentTypeCharset静态方法
	 */
	protected static Pattern contentTypeCharset = Pattern.compile("(?<=CHARSET\\=)[a-z0-9\\-]+", Pattern.CASE_INSENSITIVE);
	
	/**
	 * 探测HttpClient返回报文中，是否设置编码格式
	 * @param response
	 * @return
	 */
	public static Charset getContentTypeCharset(CloseableHttpResponse response){
		Charset charset = null;
		Header contentType = response.getFirstHeader("Content-Type");
		if(contentType != null){
			charset = detectCharset(contentType.getValue());
		}
		return charset;
	}
	
	protected static Charset detectCharset(String str){
		Charset charset = null;
		Matcher m = contentTypeCharset.matcher(str);
		if(m.find()){
			String set = m.group().trim().toUpperCase();
			charset = Charset.forName(set);
		}
		return charset;
	}

	
	public static boolean hasAlphaBeta(String str) {
		if(StrKit.isBlank(str)) return false;
		Pattern p = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		return m.find();
	}
	
	public static boolean isValidIdcard(String idcard) {
		if(StrKit.isBlank(idcard)) return false;
		idcard = idcard.toUpperCase();
		//总体格式检查
		Pattern p = Pattern.compile("^[0-9]{17}[0-9X]$");
		if(p.matcher(idcard).find() == false) {
			return false;
		}
		
		//省码检查
		Integer[] provinceCode = {11, 12, 13, 14, 15,
				  21,22,23,
				  31,32,33,34,35,36,37,
				  41,42,43,44,45,46,
				  50,51,52,53,54,
				  61,62,63,64,65,
				  }; //81,82港澳略
		List<Integer> prvList = Arrays.asList(provinceCode);
		Integer prv = Integer.valueOf(idcard.substring(0, 2));
		if(!prvList.contains(prv)) {
			return false;
		}
		
		//生日检查
		SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyyMMdd");
		sdfYmd.setLenient(false);
		try {
			sdfYmd.parse(idcard.substring(6, 14));
		} catch (ParseException e) {
			return false;
		}
		
		//校验码检查
		int[] weight = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
		int convolve = 0;
		for(int i = 0; i< 17; i++) {
			char ch = idcard.charAt(i);
			int v = ch - '0';
			convolve += v * weight[i];
		}
		int remaider = convolve % 11;
		char[] remaiderMap = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};		
		return idcard.charAt(17) == remaiderMap[remaider];
	}
	
	
	public static String toMedia(String url){
		String ret = "";
		if(StrKit.isBlank(url)) return "";
		if(domain == null){
			PropKit.use("jpress.properties");
			domain = PropKit.get("domain", "");
			if(StringUtils.endsWith(domain, "/")){
				domain = domain.substring(0, domain.length()-1);
			}
		}
		if(StringUtils.startsWith(url, "http")) ret = url;
		else{
			String slash = "";
			if(!StringUtils.startsWith(url, "/")) slash = "/";
			ret = domain + slash + url;
		}
		return ret;
	}
	
	public static String stripMultiMedia(String urls){
		if(StrKit.isBlank(urls)) return "";
		
		String[] urlAry = urls.split(",");
		String newUrls = "";
		for(String url : urlAry){
			newUrls += stripMedia(url) + ",";
		}
		if(StringUtils.endsWith(newUrls, ",")){
			newUrls = newUrls.substring(0, newUrls.length()-1);
		}
		return newUrls;
	}
	
	public static String stripMedia(String url){
		String ret = "";
		if(StrKit.isBlank(url)) return "";
		if(domain == null){
			PropKit.use("jpress.properties");
			domain = PropKit.get("domain", "");
			if(StringUtils.endsWith(domain, "/")){
				domain = domain.substring(0, domain.length()-1);
			}
		}
		
		if(StringUtils.startsWith(url, domain)){
			ret = url.substring(domain.length());
		} else {
			String slash = "";
			if(!StringUtils.startsWith(url, "/")) slash = "/";
			ret = slash + url;
		}
		return ret;
	}

}
