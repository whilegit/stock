package yjt.api.v1;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.jfinal.aop.Before;

import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.EncryptUtils;
import io.jpress.utils.StringUtils;
import yjt.model.query.ContractQuery;

@RouterMapping(url="/v1")
@Before(AccessTokenInterceptor.class)
public class IndexController extends ApiBaseController {
	
	protected static final SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy-MM-dd");
	
	public void login(){
		String mobile = getPara("mobile");
		String password = getPara("password");
		if(!StringUtils.areNotBlank(mobile, password)){
			renderJson(getReturnJson(Code.ERROR, "手机号码或密码不能为空", EMPTY_OBJECT));
			return;
		}
		User user = UserQuery.me().findUserByMobile(mobile);
		if(user == null){
			renderJson(getReturnJson(Code.ERROR, "手机号码或密码错误", EMPTY_OBJECT));
			return;
		}
		if(EncryptUtils.verlifyUser(user.getPassword(), user.getSalt(), password)){
			double income = ContractQuery.me().queryDebits(user.getId());
			double outcome = ContractQuery.me().queryCredits(user.getId());
			Date birthday = user.getBirthday();
			String birthdayStr = (birthday != null) ? sdfYmd.format(birthday) : "";
			
			HashMap<String, Object> profile = new HashMap<String, Object>();
			profile.put("memberID", user.getId().toString());
			profile.put("avatar", user.getAvatar());
			profile.put("mobile", user.getMobile());
			profile.put("nickname", user.getNickname());
			profile.put("name", user.getRealname());
			profile.put("gender", user.getGender());
			profile.put("birthday", birthdayStr);
			profile.put("score", ""+user.getScore());
			profile.put("income", ""+income);
			profile.put("outcome", ""+outcome);
			profile.put("memberToken", user.getMemberToken());
					
			renderJson(getReturnJson(Code.OK, "", profile));
		}else{
			renderJson(getReturnJson(Code.ERROR, "手机号码或密码错误", EMPTY_OBJECT));
		}
		
	}
	
	@Before(MemberTokenInterceptor.class)
	public void index(){
		renderJson(getReturnJson(Code.OK, "Hello Index", EMPTY_OBJECT));
	}
	
	@Before(MemberTokenInterceptor.class)
	public void userInfo(){
		BigInteger userID = getParaToBigInteger("memberID");
		User user = UserQuery.me().findByIdNoCache(userID);
		if(user == null){
			renderJson(getReturnJson(Code.ERROR, "用户不存在", EMPTY_OBJECT));
			return;
		}
		
		Date birthday = user.getBirthday();
		String birthdayStr = (birthday != null) ? sdfYmd.format(birthday) : "";

		//仅查询正在还款期、展期的总金额，损失类的金额已核销在此不统计
		double income = ContractQuery.me().queryDebits(user.getId());
		double outcome = ContractQuery.me().queryCredits(user.getId());
		HashMap<String, Object> profile = new HashMap<String, Object>();
		profile.put("memberID", user.getId().toString());
		profile.put("avatar", user.getAvatar());
		profile.put("mobile", user.getMobile());
		profile.put("nickname", user.getNickname());
		profile.put("name", user.getRealname());
		profile.put("gender", user.getGender());
		profile.put("birthday", birthdayStr);
		profile.put("score", ""+user.getScore());
		profile.put("income", ""+income);
		profile.put("outcome", ""+outcome);
		
		renderJson(getReturnJson(Code.OK, "", profile));
	}
}
