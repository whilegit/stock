package yjt.api.v1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.upload.MultipartRequest;

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
		
		if(value == null){
			
			if(isMultipartRequest()){
				try {
					HttpServletRequest request = getRequest();
					if (request instanceof MultipartRequest == false){
						MultipartRequest mrequest = new MultipartRequest(getRequest());
						value = mrequest.getParameter(field);
					}
						
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return value;
	}
	
	public void handleRequest(HttpServletRequest request) throws ServletException  
	{  
  
	     // DiskFileItem工厂,主要用来设定上传文件的参数  
	     DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();  
	  
	     // 上传文件所用到的缓冲区大小,超过此缓冲区的部分将被写入到磁盘  
	     fileItemFactory.setSizeThreshold(1024*1024);  
	  
	     // 上传文件用到的临时文件存放位置  
	     fileItemFactory.setRepository(this.getRepository(ac));  
	  
	     // 使用fileItemFactory为参数实例化一个ServletFileUpload对象  
	     // 注意:该对象为commons-fileupload-1.2新增的类.  
	     // 对于1.2以下的commons-fileupload版本并不存在此类.  
	     ServletFileUpload upload = new ServletFileUpload(fileItemFactory);  
	  
	     // 从session中读取对本次上传文件的最大值的限制  
	     String maxUploadSize = (String)request.getSession().  
	getAttribute(BasicConstants.maxUploadSize);  
	  
	     // 获取struts-config文件中controller标签的maxFileSize属性来确定默认上传的限制  
	     // 如果struts-config文件中controller标签的maxFileSize属性没设置则使用默认的上传限制250M.  
	     long defaultOrConfigedMaxUploadSize = this.getSizeMax(ac);  
	     if (maxUploadSize != null && maxUploadSize != "")  
	     {  
	          // 如果maxUploadSize设定不正确则上传限制为defaultOrConfigedMaxUploadSize的值  
	  
	          // 正确则为maxUploadSize转换成的字节数  
	          upload.setSizeMax((long) this.convertSizeToBytes(  
	maxUploadSize, defaultOrConfigedMaxUploadSize));  
	        
	      }  
	      else  
	      {  
	          // 如果maxUploadSize没设置则使用默认的上传限制   
	          upload.setSizeMax(defaultOrConfigedMaxUploadSize);  
	      }   
	  
	      // 从session中清空maxUploadSize  
	      request.getSession().removeAttribute("maxUploadSize");   
	  
	      // Create the hash tables to be populated.  
	      elementsText = new Hashtable();  
	      elementsFile = new Hashtable();  
	      elementsAll = new Hashtable();   
	  
	      // Parse the request into file items.  
	      List items = null;  
	      // ServletFileUpload类来处理表单请求  
	      // 抛出的异常为FileUploadException的子异常  
	      // 如果捕获这些异常就将捕获的异常放到session中返回.  
	  
	      try  
	      {  
	            items = upload.parseRequest(request);  
	  
	      }  
	      catch (FileUploadBase.SizeLimitExceededException e)  
	      {  
	  
	            // 请求数据的size超出了规定的大小.  
	            request.getSession().setAttribute(  
	                BasicConstants.baseSizeLimitExceededException, e);  
	            return;  
	      }  
	      catch (FileUploadBase.FileSizeLimitExceededException e)  
	      {  
	            // 请求文件的size超出了规定的大小.  
	            request.getSession().setAttribute(  
	                BasicConstants.baseFileSizeLimitExceededException, e);  
	            return;  
	      }  
	      catch (FileUploadBase.IOFileUploadException e)  
	      {  
	            // 文件传输出现错误,例如磁盘空间不足等.  
	            request.getSession().setAttribute(  
	                BasicConstants.baseIOFileUploadException, e);  
	            return;  
	      }  
	      catch (FileUploadBase.InvalidContentTypeException e)  
	      {  
	            // 无效的请求类型,即请求类型enctype != "multipart/form-data"  
	            request.getSession().setAttribute(  
	                BasicConstants.baseInvalidContentTypeException, e);  
	            return;  
	      }  
	      catch (FileUploadException e)  
	      {  
	           // 如果都不是以上子异常,则抛出此总的异常,出现此异常原因无法说明.  
	           request.getSession().setAttribute(  
	                BasicConstants.FileUploadException, e);  
	           return;  
	      }  
	  
	      // Partition the items into form fields and files.  
	      Iterator iter = items.iterator();  
	        
	      while (iter.hasNext())  
	      {  
	            FileItem item = (FileItem) iter.next();  
	  
	            if (item.isFormField())  
	            {  
	                   addTextParameter(request, item);  
	            }  
	            else  
	            {  
	                   addFileParameter(item);  
	            }  
	      }  
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
