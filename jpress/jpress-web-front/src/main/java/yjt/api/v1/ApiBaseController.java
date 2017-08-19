package yjt.api.v1;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.kit.StrKit;

import io.jpress.core.BaseFrontController;

public class ApiBaseController extends BaseFrontController{

	//空对象，用于生成json时用{}或[]或""替换null
	public final static class EmptyClass{}
	public final static EmptyClass EMPTY_OBJECT = new EmptyClass();
	public final static Object[]   EMPTY_ARRAY = new Object[0];
	public final static String     EMPTY_STRING = "";
	

	protected ApiReturnType getReturnJson(Code errno, String msg, Object data){
		ApiReturnType art = new ApiReturnType(errno, msg, data); 
		return art;
	}
	
	protected void accessTokenFail(){
		ApiReturnType art = getReturnJson(Code.ERROR, "accessToken失效", EMPTY_OBJECT);
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
