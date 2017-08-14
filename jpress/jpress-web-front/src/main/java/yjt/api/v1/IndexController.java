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
import yjt.model.Follow;
import yjt.model.query.ContractQuery;
import yjt.model.query.FollowQuery;

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
			
			String memberToken = getRandomString(32);
		    user.setMemberToken(memberToken);
		    user.update();
		    profile.put("memberToken", memberToken);
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
	
	@Before(MemberTokenInterceptor.class)
	public void followUser(){
		BigInteger memberID = getParaToBigInteger("memberID");
		BigInteger userID = getParaToBigInteger("userID");
		User user = UserQuery.me().findByIdNoCache(userID);
		if(user == null){
			renderJson(getReturnJson(Code.ERROR, "用户不存在", EMPTY_OBJECT));
			return;
		}
		boolean flag = false;
		Follow follow = FollowQuery.me().getFollow(userID, memberID);
		if(follow == null){
			//新建一条关注
			follow = getModel(Follow.class);
			follow.setFollowedId(userID);
			follow.setFollowerId(memberID);
			follow.setStatus(Follow.Status.FOLLOWED.getIndex());
			follow.setChangeTime(new Date());
			boolean f = follow.save();
			if(f == false) {
				renderJson(getReturnJson(Code.ERROR, "新建关注失败", EMPTY_OBJECT));
				return;
			}
			flag = true;
		}else if(follow.getStatus() == Follow.Status.UNFOLLOWED.getIndex()){
			//现在关注对方
			follow.setStatus(Follow.Status.FOLLOWED.getIndex());
			follow.setChangeTime(new Date());
			boolean f =follow.update();
			if(f == false) {
				renderJson(getReturnJson(Code.ERROR, "重新关注失败", EMPTY_OBJECT));
				return;
			}
			flag = true;
		}else{
			//现在取消关注
			follow.setStatus(Follow.Status.UNFOLLOWED.getIndex());
			follow.setChangeTime(new Date());
			boolean f =follow.update();
			if(f == false) {
				renderJson(getReturnJson(Code.ERROR, "取消关注失败", EMPTY_OBJECT));
				return;
			}
			flag = false;
		}
		HashMap<String, Object> profile = new HashMap<String, Object>();
		profile.put("userID", userID.toString());
		profile.put("userName", user.getRealname());
		profile.put("userAvatar", user.getAvatar());
		profile.put("userMobile", user.getMobile());
		profile.put("isFollowed", flag ? "1" : "0");

		renderJson(getReturnJson(Code.OK, "", profile));
	}
	
}
