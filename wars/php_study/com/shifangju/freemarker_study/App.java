package com.shifangju.freemarker_study;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import unionpay.Shoufujie;
import unionpay.UnionAppPay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shifangju.freemarker_study.Amap.Regeo;

import Verify.IdcardVerify;
import Verify.MobileVerify;

/**
 * Hello world!
 *
 */


public class App 
{
	public static void exeCmd(String[] commandStr) {  
        BufferedReader br = null;  
        try {  
            Process p = Runtime.getRuntime().exec(commandStr);  
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));  
            String line = null;  
            StringBuilder sb = new StringBuilder();  
            while ((line = br.readLine()) != null) {  
                sb.append(line + "\n");  
            }  
            //System.out.println(sb.toString());  
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
	 * 
	 * @param args
	 * @throws IOException
	 * @throws TemplateException
	 * @throws IM4JavaException 
	 * @throws InterruptedException 
	 */
	public static void main( String[] args ) throws IOException, TemplateException, InterruptedException, IM4JavaException
    {
		/*
		IMOperation operation = new IMOperation();
		
		operation.addImage("E:\\borrow-agreement.png"); 
		operation.addImage("E:\\water.png");   
		operation.gravity("northwest");
		operation.geometry(1239,6200, 100, 200);  
		operation.composite();
		//operation.append();
		operation.addImage("E:\\borrow-agreement-rotate90.png");    
        

		//Windows中不能正确运行，尝试解注此行。
		//cmd.setSearchPath("C:\\Program Files\\ImageMagick-7.0.7-Q8");  
		//如果还是报错，查看一下两个环境变量：
		//  PATH=C:\\Program Files\\ImageMagick-7.0.7-Q8;...
		//  IM4JAVA_TOOLPATH=C:\Program Files\ImageMagick-7.0.7-Q8;
		//如果还是报错，尝试将ImageMagick安装目录下的magick.exe改成convert.exe
		ConvertCmd cmd = new ConvertCmd();
		cmd.run(operation);
		operation.closeOperation();
		
		String cmd = "magick E:/borrow-agreement.png -font E:/msyh.ttf -pointsize 20 -draw \"text 360,350 \'SN20170907153000123456\'\" -draw \"text 485,410 \'毛泽东\'\" -draw \"text 300,467 \'331081198407237619\'\" -draw \"text 485,525 \'蒋介石\'\" -draw \"text 300,580 \'331081198407237619\'\" -draw \"text 420,5740 \'蒋介石\'\" -draw \"text 280,5775 \'2017\'\" -draw \"text 395,5775 \'09\'\" -draw \"text 476,5775 \'07\'\" -draw \"text 420,5852 \'毛泽东\'\" -draw \"text 280,5887 \'2018\'\" -draw \"text 395,5887 \'08\'\" -draw \"text 476,5887 \'08\'\" -draw \"text 280,6000 \'2019\'\" -draw \"text 400,6000 \'09\'\" -draw \"text 480,6000 \'09\'\" E:/r_.png";
		File f = new File("E:\\cmd.bat");
		if(f.exists() == false) f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(cmd.getBytes("GBK"));
		fos.flush();
		fos.close();
		String[] cmds = {"cmd","/C", f.getAbsolutePath()};
		exeCmd(cmds);
		*/
		
    	//freemarker();
    	//fastjson_test();
		
		//httpclient_test();
		//fast();
		/*
		 * $order_info = array(
	'frontUrl' => 'http://localhost:8086/upacp_demo_app/demo/api_05_app/FrontReceive.php', //前台通知地址
	'backUrl' => 'http://222.222.222.222:8080/upacp_demo_app/demo/api_05_app/BackReceive.php',  //后台通知地址
	'merId' => '777290058110048',    //商户代码，请改自己的测试商户号
	'orderId' => '20170909090602',  //商户订单号，8-32位数字字母，不能含“-”或“_”
	'txnTime' => '20170909090602',  //订单发送时间，格式时YYYYMMDDhhmmss
	'txnAmt' => '1000',   //交易金额，单位分
);

		 */
		/*
		System.out.println("-----------");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("merId", "777290058110048");
		params.put("orderId", "20170909090602");
		params.put("txnTime", "20170909090602");
		params.put("txnAmt", "1000");
		
		UnionAppPay appPay = new UnionAppPay();
		Map<String, String> result = appPay.comsume(params);
		if(result == null){
			System.out.println("获取tn码失败");
		} else {
			System.out.println("获取tn码成功, tn=" + result.get("tn"));
		}
		*/
		/*
		Regeo regeo = Amap.trans(28.0, 121.0);
		if(regeo == null){
			System.out.println("regeo失败");
		} else {
			System.out.println("regeo成功");
			System.out.println(JSON.toJSONString(regeo));
		}
		*/
		Shoufujie.init();
		//System.out.println("MyQuartz.test()");
		//MyQuartz.test();
		/*
		String idcard = "331081198407237619";
		boolean flag = isValidIdcard(idcard);
		if(flag){
			System.out.println("合法的身份证号码");
		} else {
			System.out.println("非法的身份证号码");
		}
		*/
    }
	
	//{"status":"0","msg":"ok","result":{"idcard":"331081198407237619","realname":"林忠仁","province":"浙江","city":"台州","town":"温岭市","sex":"男","birth":"1984年07月23日","verifystatus":"0","verifymsg":"恭喜您，身份证校验一致！"}}

	public static void fast(){
		
		boolean flag = MobileVerify.verify("410184198501181235", "15158825888", "张先生", null);
		if(flag == false){
			System.out.println("手机号不正确");
		} else {
			System.out.println("手机号正确");
		}
	}
	
	public static boolean isValidIdcard(String idcard) {

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

	
	public static void httpclient_test(){
		String url = "http://www.163.com/";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", "vip"));
		
		try {
			String result = get(url, null, null);
			System.out.println(result.substring(0, 1024));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		
		String strParam = "";
		for(NameValuePair nvp : params){
			strParam += nvp.getName() + "=" + nvp.getValue() + "&";
		}
		strParam = strParam.substring(0, strParam.length() - 1);
		System.out.println(strParam);
		httpPost.setEntity(new StringEntity(strParam, "UTF-8"));
		
		
		//httpPost.setEntity(new UrlEncodedFormEntity(params));
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
				/*
				query += "&" + URLEncoder.encode(nvp.getName(), "UTF-8") + "=" + URLEncoder.encode(nvp.getValue(), "UTF-8");
				*/
				query += "&" + nvp.getName() + "=" + nvp.getValue();
			}
			
			if(url.indexOf('?') < 0){
				query = query.substring(1, query.length());
				url = url + "?" + query;
			} else {
				url = url + query;
			}
		}
		//System.out.println(url);
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
	protected static Charset getContentTypeCharset(CloseableHttpResponse response){
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

	private static void freemarker() throws IOException, TemplateException{
		Configuration conf = new Configuration(Configuration.VERSION_2_3_26);
    	String tplRoot = App.class.getClassLoader().getResource("templates").getPath();
    	System.out.println(tplRoot);
    	conf.setDirectoryForTemplateLoading(new File(tplRoot));
    	conf.setDefaultEncoding("UTF-8");
        
        conf.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        conf.setLogTemplateExceptions(false);
        
        System.out.println("Freemarker is started...");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("user", "Big Joe");
        Template tpl = conf.getTemplate("test.html");
        
        FileOutputStream fos = new FileOutputStream(new File("E:\\www\\svng\\sfjmall\\freemarker.html"));
        Writer out = new OutputStreamWriter(fos);
        //Writer out = new OutputStreamWriter(System.out);
        //Writer outt = new PrintWriter(new File("/"));
        tpl.process(root, out);
        out.close();
        fos.close();
        
        System.out.println( "Freemarker is finished." );
	}
	
	private static void fastjson_test(){
		Group grp = fastjson_decode_object_test();
    	System.out.println("json String 解码到对象：" + grp.toString());
    	String jsonStr = JSON.toJSONString(grp);
    	System.out.println("重新输入到json String: " + jsonStr + "\r\n");
    	
    	Map<String, Object> map = fastjson_decode_map_test();
    	Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
    	while(iter.hasNext()){
    		Entry<String, Object> e = iter.next();
    		System.out.println(e.getKey() + " => " + e.getValue());
    	}
    	
    	List<User> lst = fastjson_decode_array_test();
    	Iterator<User> iter_lst = lst.iterator();
    	while(iter_lst.hasNext()){
    		User u = iter_lst.next();
    		System.out.println(u.toString());
    	}
    	
    	jsonStr = JSON.toJSONString(lst);
    	System.out.println(jsonStr);
	}
	static class Group{
		private int groupId;
		private String groupName;
		private List<User> users;
		
		public int getGroupId() { return groupId;	}
		public void setGroupId(int groupId){ this.groupId = groupId;}
		public String getGroupName() { return groupName; }
		public void setGroupName(String groupName){this.groupName = groupName;}
		public List<User> getUsers() { return users; }
		public void setUsers(List<User> users) { this.users = users; }
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			String usersStr = "[";
			if(users != null){
				for(User u : users){
					usersStr += u.toString() + ",";
				}
			}
			usersStr += "]";
			return "groupId = "+groupId + ", groupName = " + groupName + ", users = " + usersStr;
		}
		
	}
	
	static class User{
		private int uid;
		private String userName;
		private int userGender;
		public int getUid() {	return uid;}
		public void setUid(int uid) { this.uid = uid; }
		public String getUserName() { return userName; }
		public void setUserName(String userName) { this.userName = userName; }
		public int getUserGender() { return userGender; }
		public void setUserGender(int userGender) { this.userGender = userGender; }
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "{uid = " + uid + ", userName = " + userName + ", userGender = " + userGender +"}";
		}
	}
	
	private static Group fastjson_decode_object_test(){
		String jsonStr = 
				 "{" + "\r\n"
				+ 	"\t\"groupId\":1,"+ "\r\n"
				+ 	"\t\"groupName\":\"Lin's\","+ "\r\n"
				+ 	"\t\"users\":["+ "\r\n"
				+ 	"\t\t{\"uid\":10086,\"userName\":\"LinZhongren\",\"userGender\":1},"+ "\r\n"
				+ 	"\t\t{\"uid\":10001,\"userName\":\"LinHehe\",\"userGender\":2},"+ "\r\n"
				+ 	"\t]"+ "\r\n"
				+"}";
		System.out.println("手工json String: \r\n" + jsonStr);
		Group grp = JSON.parseObject(jsonStr, Group.class);
		return grp;
	}
	
	private static Map<String, Object> fastjson_decode_map_test(){
		String jsonStr = 
				"{" + "\r\n"
				+	"\t\"one\":\"1\","
				+	"\t\"two\":\"2\","
				+	"\t\"three\":\"3\","
				+   "\t\"four\":6"
				+"}";
		JSONObject jsonObj = JSONObject.parseObject(jsonStr);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JSONObject.toJavaObject(jsonObj, Map.class);
		return map;
	}
	
	private static List<User> fastjson_decode_array_test(){
		String jsonStr = "["+
					"\t{\"uid\":10086,\"userName\":\"LinZhongren1\",\"userGender\":1},"+ "\r\n" +
					"\t{\"uid\":10085,\"userName\":\"LinZhongren2\",\"userGender\":1},"+ "\r\n" +
				"]";
		List<User> list = JSONObject.parseArray(jsonStr, User.class);
		return list;
	}
}
