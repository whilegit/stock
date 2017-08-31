package yjt.api.v1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.upload.MultipartRequest;
import com.jfinal.upload.UploadFile;

import io.jpress.core.BaseFrontController;
import io.jpress.model.query.OptionQuery;
import io.jpress.template.TemplateManager;
import io.jpress.template.Thumbnail;
import io.jpress.utils.AttachmentUtils;
import io.jpress.utils.FileUtils;
import io.jpress.utils.ImageUtils;
import yjt.api.v1.Annotation.UploadAnnotation;

public class ApiBaseController extends BaseFrontController{

	protected static final Log log = Log.getLog(ApiBaseController.class);
	
	//空对象，用于生成json时用{}或[]或""替换null
	public final static class EmptyClass{}
	public final static EmptyClass EMPTY_OBJECT = new EmptyClass();
	public final static Object[]   EMPTY_ARRAY = new Object[0];
	public final static String     EMPTY_STRING = "";
	protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	protected ApiReturnType getReturnJson(Code errno, String msg, Object data){
		ApiReturnType art = new ApiReturnType(errno, msg, data); 
		return art;
	}
	
	public void accessTokenFail(String accessToken){
		ApiReturnType art = getReturnJson(Code.ERROR, StrKit.isBlank(accessToken) ? "缺少accessToken" : "accessToken失效", EMPTY_OBJECT);
		this.renderJson(art);
	}
	
	public void paramError(String err){
		ApiReturnType art = getReturnJson(Code.ERROR, err, EMPTY_OBJECT);
		this.renderJson(art);
	}
	
	public void paramError(String err, Code code){
		ApiReturnType art = getReturnJson(code, err, EMPTY_OBJECT);
		this.renderJson(art);
	}
	
	
	
	public void memberTokenFail(){
		ApiReturnType art = getReturnJson(Code.TIMEOUT, "memberToken失效", EMPTY_OBJECT);
		this.renderJson(art);
	}
	
    protected static String getRandomString(int length) { //length表示生成字符串的长度  
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";     
        Random random = new Random();     
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();     
     }     
	
	protected ApiReturnType getReturnJson(Code errno, Object data){
		return getReturnJson(errno, EMPTY_STRING, data);
	}
	

	
	public class ApiReturnType{
		int success;
		String msg;
		Object data;
		ApiReturnType(Code success, String msg, Object data){
			this.success = success.getIndex();
			this.msg = msg;
			this.data = data;
		}
		public int getsuccess() {
			return success;
		}
		public void setsuccess(int success) {
			this.success = success;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public Object getData() {
			return data;
		}
		public void setData(Object data) {
			this.data = data;
		}
	}
	
	@Override
	public boolean isMultipartRequest(){
		boolean flag = false;
		if(super.isMultipartRequest()){
			HttpServletRequest request = this.getRequest();
			Invocation inv = (Invocation) request.getAttribute("invocation");
			if(inv != null){
				Method method = inv.getMethod();
				if(method != null){
					UploadAnnotation anno = method.getAnnotation(UploadAnnotation.class);
					flag = (anno != null);
				}
			}
		}
		return flag;
	}
	
	@Override
	public String getPara(String field){
		String value = super.getPara(field);
		if(value != null) return value;
		if(!isMultipartRequest()) return null;

		HttpServletRequest request = getRequest();
		if(request.getAttribute("MUTLIPART_CHECK") == null) parseMultipartRequest();
		@SuppressWarnings("unchecked")
		HashMap<String, String> formFields = (HashMap<String, String>) request.getAttribute("MUTLIPART_FORM_FIELDS");
		return formFields.get(field);
	}
	
	protected void parseMultipartRequest(){
		System.out.println("parseMultipartRequest");
		HttpServletRequest request = getRequest();
		request.setAttribute("MUTLIPART_CHECK", true);
		HashMap<String, String> formFields = new HashMap<String, String>();
		ArrayList<UploadFile> uploadFiles = new ArrayList<UploadFile>();
		
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// Configure a repository (to ensure a secure temp location is used)
		ServletContext servletContext = request.getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		try {
			List<FileItem> items = upload.parseRequest(request);
			System.out.println(items.size());
			if(items != null && items.size() > 0){
				String webRoot = PathKit.getWebRootPath();
				StringBuilder uploadDir = new StringBuilder(webRoot).append(File.separator).append("attachment").append(File.separator).append("api").
						append(File.separator).append(dateFormat.format(new Date()));
						
				System.out.println(items.size());
				for(FileItem item : items){
					if(!item.isFormField()){
						String parameterName = item.getFieldName();
						String contentType = item.getContentType();
						String originalFileName = item.getName();
							    
						long sizeInBytes = item.getSize();
						if(sizeInBytes > 1024 * 1024){
							 item.delete();
							 continue;
						}
						String suffix = FileUtils.getSuffix(originalFileName);
						String uuid = UUID.randomUUID().toString().replace("-", "");
						File uploadFile = new File(uploadDir.toString() + File.separator + uuid + suffix);
						if (!uploadFile.getParentFile().exists()) {
							 uploadFile.getParentFile().mkdirs();
						}
						try {
							item.write(uploadFile);
							uploadFiles.add(new UploadFile(parameterName, uploadDir.toString(), uploadFile.getName(), originalFileName, contentType));

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						String key = item.getFieldName();
					    String val = item.getString();
					    System.out.println(key + "=>" + val);
					    if(StrKit.notBlank(key, val)){
					    	formFields.put(key, val);
					    }
					}
					item.delete();
				}
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("MUTLIPART_FORM_FIELDS", formFields);
		request.setAttribute("MUTLIPART_UPLOAD_FILES", uploadFiles);
	}
	
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAI2CyIn5cvuXf0";
    static final String accessKeySecret = "q23aGRpz0v6y1q2L1IwuQWaqw9gXA6";
    
	public static SendSmsResponse sendSms(String mobile, String template, JSONObject json) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mobile);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("林忠仁");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(template);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(json.toJSONString());

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UploadFile> getFiles() {

		if(!isMultipartRequest()){
			return new ArrayList<UploadFile>();
		} else {
			HttpServletRequest request = getRequest();
			return (List<UploadFile>) request.getAttribute("MUTLIPART_UPLOAD_FILES");
		}
	}
	
	public static enum Code{
		OK("成功",1), ERROR("失败",0), TIMEOUT("登陆失效",-1);
		private String name;
		private int index;
	    private Code(String name, int index) {  
	        this.name = name;  
	        this.index = index;  
	    }
	    // 普通方法  
	    public static Code getEnum(int index){
	    	for (Code s : Code.values()) {  
	            if (s.getIndex() == index) {  
	                return s;  
	            }  
	        }
	        return null; 
	    }
	    
		public String getName() {	return name;	}
		public void setName(String name) {	this.name = name;	}
		public int getIndex() {	return index;	}
		public void setIndex(int index) {	this.index = index;	}
	   
	}
}
