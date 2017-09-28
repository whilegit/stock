package yjt.api.v1.Interceptor;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import yjt.api.v1.ApiBaseController;
import yjt.api.v1.ApiBaseController.Code;
import yjt.api.v1.Annotation.ParamAnnotation;
import yjt.Utils;


public class ParamInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		boolean pass = true;
		ApiBaseController bc = (ApiBaseController) inv.getController();
		HttpServletRequest request = bc.getRequest();
		request.setAttribute("invocation", inv);
		
		String err = "";
		Code code = Code.TIMEOUT;
		ParamAnnotation[] annos = inv.getMethod().getAnnotationsByType(ParamAnnotation.class);
		for(ParamAnnotation anno : annos){
			Type type = anno.type();
			boolean must = anno.must();
			String name = anno.name();
			String chs = anno.chs();
			int minlen = anno.minlen();
			int maxlen = anno.maxlen();
			
			String value = bc.getPara(name);
			if(StrKit.isBlank(value)){
				if(must){
					String mustErr = anno.mustErrTips();
					err = StrKit.notBlank(mustErr) ? mustErr : chs + "不能为空";
					pass = false;
					code = Code.ERROR;
					break;
				}else{
					continue;
				}
			}
			
			String typeErrTips = anno.typeErrTips();
			String minErrTips = anno.minErrTips();
			String maxErrTips = anno.maxErrTips();
			String minlenErrTips = anno.minlenErrTips();
			String maxlenErrTips = anno.maxlenErrTips();
			String alllistErrTips = anno.allowListErrTips();
			switch(type){
				case INT:
					Integer v = null;
					try{
						v = Integer.parseInt(value);
					} catch (Exception e){
						err = StrKit.notBlank(typeErrTips) ? typeErrTips : chs+"错误";
						pass = false;
						code = Code.ERROR;
						break;
					}
					if(v < anno.min()){
						err = StrKit.notBlank(minErrTips) ? minErrTips :  chs + "不少于" + anno.min();
						pass = false;
						code = Code.ERROR;
						break;
					}
					if(v > anno.max()){
						err = StrKit.notBlank(maxErrTips) ? maxErrTips :  chs + "不大于" + anno.max();
						pass = false;
						code = Code.ERROR;
						break;
					}
					break;
				case MOBILE:
					if(!Utils.isMobile(value)){
						err = StrKit.notBlank(typeErrTips) ? typeErrTips : chs+"格式错误";
						pass = false;
						code = Code.ERROR;
					}
					break;
				case STRING:
					value = value.trim();
					int len = value.length();
					if(len < minlen){
						err = StrKit.notBlank(minlenErrTips) ? minlenErrTips : chs + "至少" + minlen + "位";
						pass = false;
						code = Code.ERROR;
					}
					if(len > maxlen) {
						err = StrKit.notBlank(maxlenErrTips) ? maxlenErrTips : chs + "不超过" + maxlen + "位";
						pass = false;
						code = Code.ERROR;
					}
					break;
				case MEMBER_TOKEN:
					BigInteger memberID = bc.getParaToBigInteger("memberID"); 
					String memberToken = bc.getPara("memberToken");
					if(StrKit.isBlank(memberToken) == true || memberID == null){
						pass = false;
						code = Code.ERROR;
						if(memberID == null)
						err = (memberToken == null) ? "缺少用户令牌" : "缺少用户编号";
						break;
					}
					User user = UserQuery.me().findByIdNoCache(memberID);
					if(user == null){
						pass = false;
						code = Code.TIMEOUT;
						err = "登录已失效，请重新登录";
						break;
					}
					if(memberToken.equals(user.getMemberToken()) == false){
						pass = false;
						code = Code.TIMEOUT;
						err = "登录已失效，请重新登录";
						break;
					}
					break;
				case DATE:
					if(Utils.getYmd(value) == null){
						pass = false;
						code = Code.ERROR;
						err = StrKit.notBlank(typeErrTips) ? typeErrTips : chs+"错误";
						break;
					}
					break;
				case DOUBLE:
					Double d = null;
					try{
						d = Double.parseDouble(value);
					} catch(Exception e) {
						pass = false;
						code = Code.ERROR;
						err = StrKit.notBlank(typeErrTips) ? typeErrTips : chs+"错误";
						break;
					}
					
					if(d < anno.mind()) {
						pass = false;
						code = Code.ERROR;
						err = StrKit.notBlank(minErrTips) ? minErrTips : chs + "过小";
						break;
					} else if(d > anno.maxd()) {
						pass = false;
						code = Code.ERROR;
						err = StrKit.notBlank(maxErrTips) ? maxErrTips : chs + "过大";
						break;
					}
					break;
				case ENUM_STRING:
					List<String> allow_list = Arrays.asList(anno.allow_list());
					if(!allow_list.contains(value)) {
						pass = false;
						code = Code.ERROR;
						err = StrKit.notBlank(alllistErrTips) ? alllistErrTips : chs + "错误";
						break;
					}
					break;
				default:
					break;
			}
			
			if(pass == false){
				break;
			}
		}
		if(pass){
			inv.invoke();
		}
		else {
			bc.paramError(err, code);
		}
	}
	
	public static enum Type{
		INT("整数",1), MOBILE("手机号",2), STRING("字符串", 3), MEMBER_TOKEN("用户令牌",4), DATE("日期", 5), 
		DOUBLE("小数", 6), ENUM_STRING("字符串数组", 7);
		private String name;
		private int index;
	    private Type(String name, int index) {  
	        this.name = name;  
	        this.index = index;  
	    }
		public String getName() {	return name;	}
		public void setName(String name) {	this.name = name;	}
		public int getIndex() {	return index;	}
		public void setIndex(int index) {	this.index = index;	}
	}
}