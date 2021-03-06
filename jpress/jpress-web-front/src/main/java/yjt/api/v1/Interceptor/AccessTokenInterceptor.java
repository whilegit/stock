package yjt.api.v1.Interceptor;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

import yjt.api.v1.ApiBaseController;;
public class AccessTokenInterceptor implements Interceptor{

	private static String ACCESSTOKEN_SALT = "yjt./*?";
	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		boolean pass = false;
		HttpServletRequest request = inv.getController().getRequest();
		request.setAttribute("invocation", inv);
		String accessToken = ((ApiBaseController)(inv.getController())).getPara("accessToken");
		if(StrKit.notBlank(accessToken)){
			//pass = accessToken.equals(getCurrentAccessToken());   //每天都会变
			pass = accessToken.equals("d1bf755b5579590c44bf3fb72623245f9bd5cea5");  //写死
		}

		if(pass){
			inv.invoke();
		}
		else {
			ApiBaseController bc = (ApiBaseController) inv.getController();
			bc.accessTokenFail(accessToken);
		}
	}
	
	public static String getCurrentAccessToken(){
		/*
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
		String today = sdf.format(new Date());
		String token = getMD5(ACCESSTOKEN_SALT + today);
		*/
		String token = "d1bf755b5579590c44bf3fb72623245f9bd5cea5";
		return token;
	}
	
	public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5=new BigInteger(1, md.digest()).toString(16);
            //BigInteger会把0省略掉，需补全至32位
            return fillMD5(md5);
        } catch (Exception e) {
            //throw new RuntimeException("MD5加密错误:"+e.getMessage(),e);
        }
        return null;
    }
	
	public static String fillMD5(String md5){
        return md5.length()==32?md5:fillMD5("0"+md5);
    }

}
