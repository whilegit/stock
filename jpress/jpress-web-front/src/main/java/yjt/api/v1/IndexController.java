package yjt.api.v1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.jfinal.aop.Before;

import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;

@RouterMapping(url="/v1")
@Before(AccessTokenInterceptor.class)
public class IndexController extends ApiBaseController {
	
	protected static final SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy-MM-dd");
	public void login(){
		
		renderJson(getReturnJson(Code.OK, "Hello login", EMPTY_OBJECT));
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
		}
		
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
		profile.put("income", ""+user.getDebits().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		profile.put("outcome", ""+user.getCredits().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		
		renderJson(getReturnJson(Code.OK, "", profile));
	}
}
