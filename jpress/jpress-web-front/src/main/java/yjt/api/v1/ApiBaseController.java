package yjt.api.v1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
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
	
	protected void accessTokenFail(String accessToken){
		ApiReturnType art = getReturnJson(Code.ERROR, StrKit.isBlank(accessToken) ? "缺少accessToken" : "accessToken失效", EMPTY_OBJECT);
		this.renderJson(art);
	}
	
	protected void memberTokenFail(){
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
	
	protected static Pattern MOBILE_PATTERN = Pattern.compile("^1[345789][0-9]{9}$");
	protected static boolean isMobile(String mobile){
		boolean ret = false;
		if(StrKit.notBlank(mobile)){
			Matcher m = MOBILE_PATTERN.matcher(mobile);
			ret = m.matches();
		}
		return ret;
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
	
	protected void processImage(String newPath) {
		if (!AttachmentUtils.isImage(newPath))
			return;

		if (".gif".equalsIgnoreCase(FileUtils.getSuffix(newPath))) {
			// 过滤 .gif 图片
			return;
		}

		try {
			// 由于内存不够等原因可能会出未知问题
			processThumbnail(newPath);
		} catch (Throwable e) {
			log.error("processThumbnail error", e);
		}
		try {
			// 由于内存不够等原因可能会出未知问题
			processWatermark(newPath);
		} catch (Throwable e) {
			log.error("processWatermark error", e);
		}
	}
	
	
	@Override
	public String getPara(String field){
		String value = super.getPara(field);
		if(value != null) return value;
		if(!isMultipartRequest()) return null;
		if(true) return null;
		//检查request的attribute列表里有没有此域的数据，如果之前有遍历过multipart的话相应表单项已放入attribute中
		HttpServletRequest request = getRequest();
		value = (String) request.getAttribute(field);
		if(value != null) return value;
		//该请求是Multipart请求，再检查一下请求body里有没有表单项
		if(request instanceof MultipartRequest == true) return null;
		Boolean multiflag = (Boolean) request.getAttribute("multiflag");
		//如果请求body里的表单项已经检查过了，则不再重新检查，确定
		if(multiflag != null && multiflag == true ) return null;
		
		//以下代码每个请求只执行一次
		//标记已经编历了MultipartRequest的post域
		request.setAttribute("multiflag", true);
		try {
			MultipartRequest mrequest = new MultipartRequest(getRequest());
			@SuppressWarnings("rawtypes")
			Enumeration enum1 = mrequest.getParameterNames();   
			while (enum1.hasMoreElements()) {
				String f = (String)enum1.nextElement();  
				String[] str= mrequest.getParameterValues(f);   
				for (int i=0;i<str.length;i++){   
					//只取最后一个参数
					request.setAttribute(f, str[i]);
				} 
			}
			value = (String) request.getAttribute(field);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	
	@Override
	public List<UploadFile> getFiles() {
		System.out.println("List<UploadFile> getFiles()");
		ArrayList<UploadFile> ret = new ArrayList<UploadFile>();
		HttpServletRequest request = getRequest();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(!isMultipart) {
			System.out.println(!isMultipart);
			return ret;
		}
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
			@SuppressWarnings("rawtypes")
			List items = upload.parseRequest(request);
			System.out.println(items.size());
			if(items != null && items.size() > 0){
				String webRoot = PathKit.getWebRootPath();
				StringBuilder uploadDir = new StringBuilder(webRoot).append(File.separator).append("attachment").append(File.separator).append("api").
						append(File.separator).append(dateFormat.format(new Date()));
				
				System.out.println(items.size());
				for(Object item_o : items){
					FileItem item = (FileItem) item_o;
					if(!item.isFormField()){
						//UploadFile(String parameterName, String uploadPath, String filesystemName, String originalFileName, String contentType)
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
							ret.add(new UploadFile(parameterName, uploadDir.toString(), uploadFile.getAbsolutePath(), originalFileName, contentType));

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    item.delete();
					}
				}
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	private void processThumbnail(String newPath) {
		List<Thumbnail> tbs = TemplateManager.me().currentTemplate().getThumbnails();
		if (tbs != null && tbs.size() > 0) {
			for (Thumbnail tb : tbs) {
				try {
					String newSrc = ImageUtils.scale(PathKit.getWebRootPath() + newPath, tb.getWidth(), tb.getHeight());
					processWatermark(FileUtils.removeRootPath(newSrc));
				} catch (IOException e) {
					log.error("processWatermark error", e);
				}
			}
		}
	}
	
	public void processWatermark(String newPath) {
		Boolean watermark_enable = OptionQuery.me().findValueAsBool("watermark_enable");
		if (watermark_enable != null && watermark_enable) {

			int position = OptionQuery.me().findValueAsInteger("watermark_position");
			String watermarkImg = OptionQuery.me().findValue("watermark_image");
			String srcImageFile = newPath;

			Float transparency = OptionQuery.me().findValueAsFloat("watermark_transparency");
			if (transparency == null || transparency < 0 || transparency > 1) {
				transparency = 1f;
			}

			srcImageFile = PathKit.getWebRootPath() + srcImageFile;

			File watermarkFile = new File(PathKit.getWebRootPath(), watermarkImg);
			if (!watermarkFile.exists()) {
				return;
			}

			ImageUtils.pressImage(watermarkFile.getAbsolutePath(), srcImageFile, srcImageFile, position, transparency);
		}
	}

	
	//json返回成功与否的success标志位
	protected static enum Code{
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
