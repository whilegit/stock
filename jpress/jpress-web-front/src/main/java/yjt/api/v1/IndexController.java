package yjt.api.v1;

import java.io.File;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;

import io.jpress.core.interceptor.JI18nInterceptor;
import io.jpress.model.Attachment;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.EncryptUtils;
import io.jpress.utils.FileUtils;
import io.jpress.utils.StringUtils;
import yjt.model.Captcha;
import yjt.model.Follow;
import yjt.model.query.CaptchaQuery;
import yjt.model.query.ContractQuery;
import yjt.model.query.FollowQuery;

@RouterMapping(url="/v1")
@Before(AccessTokenInterceptor.class)
@Clear(JI18nInterceptor.class)
public class IndexController extends ApiBaseController {
	
	private static final boolean DEBUG = true;
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
			HashMap<String, Object> profile = user.getMemberProfile();
			String memberToken = getRandomString(32);
		    user.setMemberToken(memberToken);
		    user.update();
		    profile.put("memberToken", memberToken);
			renderJson(getReturnJson(Code.OK, "", profile));
		}else{
			renderJson(getReturnJson(Code.ERROR, "手机号码或密码错误", EMPTY_OBJECT));
		}
		
	}
	
	@Clear()
	public void register(){
		String mobile = getPara("mobile");
		if(StrKit.isBlank(mobile)){
			renderJson(getReturnJson(Code.ERROR, "请提供手机号", EMPTY_OBJECT));
			return;
		}
		if(!isMobile(mobile)){
			renderJson(getReturnJson(Code.ERROR, "手机号格式错误", EMPTY_OBJECT));
			return;
		}
		
		String captchaStr = getPara("captcha");
		if(StrKit.isBlank(captchaStr)){
			renderJson(getReturnJson(Code.ERROR, "请提供验证码", EMPTY_OBJECT));
			return;
		}
		
		Captcha captcha = CaptchaQuery.me().getCaptcha(mobile);
		if(captcha == null){
			renderJson(getReturnJson(Code.ERROR, "验证码不存在", EMPTY_OBJECT));
			return;
		}

		if(captchaStr.equals(captcha.getCode()) == false){
			renderJson(getReturnJson(Code.ERROR, "验证码错误", EMPTY_OBJECT));
			return;
		}
		
		long sendTime = captcha.getCreateTime().getTime();
		if(sendTime + 30 * 60 * 1000 < System.currentTimeMillis()){
			renderJson(getReturnJson(Code.ERROR, "验证码超时，请重新获取", EMPTY_OBJECT));
			return;
		}
		
		User user = UserQuery.me().findUserByMobile(mobile);
		if(user != null){
			renderJson(getReturnJson(Code.ERROR, "用户已存在", EMPTY_OBJECT));
			return;
		}
		
		String password = getPara("password");
		if(StrKit.isBlank(password)){
			renderJson(getReturnJson(Code.ERROR, "请设置密码", EMPTY_OBJECT));
			return;
		}
		password = password.trim();
		if(password.length() < 6){
			renderJson(getReturnJson(Code.ERROR, "密码不少于6位", EMPTY_OBJECT));
			return;
		}
		
		String avatar = getPara("avatar");
		if(avatar == null) avatar = "";
		String salt = EncryptUtils.salt();
		String memberToken = getRandomString(32);
		
		user = getModel(User.class);
		user.setMobile(mobile);
		user.setAvatar(avatar);
		user.setPassword(EncryptUtils.encryptPassword(password, salt));
		user.setSalt(salt);
		user.setMemberToken(memberToken);
		user.save();
		
		user = UserQuery.me().findById(user.getId());
		if(user == null){
			renderJson(getReturnJson(Code.ERROR, "创建用户失败", EMPTY_OBJECT));
			return;
		}
		HashMap<String, Object> profile = user.getMemberProfile();
		profile.put("memberToken", memberToken);
		renderJson(getReturnJson(Code.OK, "", profile));
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
	
	@Before(MemberTokenInterceptor.class)
	public void updateUser(){
		BigInteger memberID = getParaToBigInteger("memberID");
		String avatar = getPara("avatar");
		String nickname = getPara("nickname");
		String name = getPara("name");
		String gender = getPara("gender");
		String birthday = getPara("birthday");
		Integer sysPush = getParaToInt("sysPush");
		Integer salePush = getParaToInt("salePush");
		Integer inPush = getParaToInt("inPush");
		Integer outPush = getParaToInt("outPush");
		
		User user = UserQuery.me().findByIdNoCache(memberID);
		if(StrKit.notBlank(avatar))	user.setAvatar(avatar);
		if(StrKit.notBlank(nickname)) user.setNickname(nickname);
		if(StrKit.notBlank(name)) user.setRealname(name);
		if(StrKit.notBlank(gender))	{
			int gendexInt = Integer.parseInt(gender);
			if(gendexInt < 0 || gendexInt >2){
				renderJson(getReturnJson(Code.ERROR, "性别设置错误", EMPTY_OBJECT));
				return;
			}
			user.setGender(gender);
		}
		if(StrKit.notBlank(birthday)){
			Date birthdayDate = null;
			try {
				birthdayDate = sdfYmd.parse(birthday);
				user.setBirthday(birthdayDate);
			} catch (ParseException e) {
				renderJson(getReturnJson(Code.ERROR, "出生年月格式错误", EMPTY_OBJECT));
				return;
			}
		}
		if(sysPush != null && (sysPush == 0 || sysPush == 1))  user.setSysPush(sysPush);
		if(salePush != null && (salePush == 0 || salePush == 1))  user.setSalePush(salePush);
		if(inPush != null && (inPush == 0 || inPush == 1))  user.setInPush(inPush);
		if(outPush != null && (outPush == 0 || outPush == 1))  user.setOutPush(outPush);

		boolean flag = user.update();
		if(flag){
			HashMap<String, Object> profile = user.getMemberProfile();
			renderJson(getReturnJson(Code.OK, "", profile));
		}else{
			renderJson(getReturnJson(Code.ERROR, "更新失败", EMPTY_OBJECT));
			return;
		}
	}
	
	/**
	 * eachFollowed  0:关注自己的（粉丝）  1:相互关注    2:自己关注的
	 */
	@Before(MemberTokenInterceptor.class)
	public void searchUser(){
		BigInteger memberID = getParaToBigInteger("memberID");
		if(memberID == null){
			renderJson(getReturnJson(Code.ERROR, "参数错误", EMPTY_OBJECT));
			return;
		}
		User member = UserQuery.me().findByIdNoCache(memberID);
		if(member == null){
			renderJson(getReturnJson(Code.ERROR, "登陆用户不存在", EMPTY_OBJECT));
			return;
		}
		String mobile = getPara("mobile");
		Integer eachFollowed = this.getParaToInt("eachFollowed");
		
		if(eachFollowed != null){
			if(eachFollowed != 0 && eachFollowed != 1 && eachFollowed != 2){
				renderJson(getReturnJson(Code.ERROR, "关注参数错误", EMPTY_OBJECT));
				return;
			}
			//以下需要包装成 {'result':[{'userID':'xxx'...},{'userID':'xxx'...}]}
			HashMap<String, Object> data = new HashMap<String, Object>();
			
			BigInteger[] ids = null;
			BigInteger[] fans = FollowQuery.me().getFollowerList(memberID);
			BigInteger[] tops = FollowQuery.me().getFollowedList(memberID);
			BigInteger[] each = FollowQuery.me().getEachFollowList(fans, tops);
			
			if(eachFollowed == 1){           //相互关注
				ids = each;
			} else if(eachFollowed == 0)  {  //自己关注的
				ids = tops;
			} else {                         //关注自己的（粉丝）
				ids = fans;
			}
			List<User> list = UserQuery.me().findList(ids);
			if(list == null || (list != null && list.size() == 0)){
				data.put("result", EMPTY_ARRAY);
				renderJson(getReturnJson(Code.OK, "", data));
				return;
			}else{
				int num = list.size();
				@SuppressWarnings("rawtypes")
				HashMap[] profiles = new HashMap[num];
				for(int i = 0; i<num; i++){
					User u = list.get(i);
					HashMap<String, Object> profile = u.getUserProfile(false);
					if(eachFollowed == 0 || eachFollowed == 1)
						profile.put("isFollowed", "1");
					else{
						//自己的粉丝不一定关注自己, 要进一步查看
						boolean flag = false;
						BigInteger uid = u.getId();
						for(BigInteger bi : each){
							if(uid.equals(bi)){
								flag = true;
								break;
							}
						}
						profile.put("isFollowed", flag? "1" : "0");
					}
					profiles[i] = profile;
				}
				data.put("result", profiles);
				renderJson(getReturnJson(Code.OK, "", data));
				return;
			}
		} else if(StrKit.notBlank(mobile)){
			if(isMobile(mobile)){
				HashMap<String, Object> data = new HashMap<String, Object>();
				//以下需要包装成 {'result':[{'userID':'xxx'...},{'userID':'xxx'...}]}
				User user = UserQuery.me().findUserByMobile(mobile);
				if(user == null){
					data.put("result", EMPTY_ARRAY);
					renderJson(getReturnJson(Code.OK, "用户不存在", data));
					return;
				}
				HashMap<String, Object> profile = user.getUserProfile(false);
				//是否已关注刚被搜索出来的对方
				Follow follow = FollowQuery.me().getFollow(user.getId(), member.getId());
				String flw = (follow != null && follow.getStatus() == Follow.Status.UNFOLLOWED.getIndex()) ? "1" : "0";
				profile.put("isFollowed", flw);
				@SuppressWarnings("rawtypes")
				HashMap[] profiles = new HashMap[1];
				profiles[0] = profile;
				data.put("result", profiles);
				renderJson(getReturnJson(Code.OK, "", data));
				return;
			}else{
				renderJson(getReturnJson(Code.ERROR, "手机号码错误", EMPTY_OBJECT));
				return;
			}
		} else {
			renderJson(getReturnJson(Code.ERROR, "无参数错误", EMPTY_OBJECT));
			return;
		}
	}
	
	public void validMobile(){
		String mobile = getPara("mobile");
		if(StrKit.isBlank(mobile)){
			renderJson(getReturnJson(Code.ERROR, "手机号格式错误", EMPTY_OBJECT));
			return;
		}
		Code code = isMobile(mobile) ? Code.OK : Code.ERROR;
		renderJson(getReturnJson(code, "", EMPTY_OBJECT));
		return;
	}
	
	public void existsMobile(){
		String mobile = getPara("mobile");
		if(StrKit.isBlank(mobile)){
			renderJson(getReturnJson(Code.ERROR, "手机号格式错误", EMPTY_OBJECT));
			return;
		}
		User user = UserQuery.me().findUserByMobile(mobile);
		Code code = (user != null) ? Code.OK : Code.ERROR;
		renderJson(getReturnJson(code, "", EMPTY_OBJECT));
		return;
	}
	
	@Before(MemberTokenInterceptor.class)
	public void updatePwd(){
		BigInteger memberID = getParaToBigInteger("memberID");
		String oldPwd = getPara("oldPwd");
		String newPwd = getPara("newPwd");
		if(!StrKit.notBlank(oldPwd, newPwd)){
			renderJson(getReturnJson(Code.ERROR, "参数不能为空", EMPTY_OBJECT));
			return;
		}
		
		newPwd = newPwd.trim();
		if(newPwd.length() < 6){
			renderJson(getReturnJson(Code.ERROR, "新密码不少于6位", EMPTY_OBJECT));
			return;
		}
		
		oldPwd = oldPwd.trim();
		User member = UserQuery.me().findByIdNoCache(memberID);
		if(!EncryptUtils.verlifyUser(member.getPassword(), member.getSalt(), oldPwd)){
			renderJson(getReturnJson(Code.ERROR, "原密码错误", EMPTY_OBJECT));
			return;
		}
		HashMap<String, Object> profile = member.getMemberProfile();
		
		String salt = EncryptUtils.salt();
		member.setPassword(EncryptUtils.encryptPassword(newPwd, salt));
		member.setSalt(salt);
		String newMemberToken = getRandomString(32);
		member.setMemberToken(newMemberToken);
		member.update();
		renderJson(getReturnJson(Code.OK, "", profile));
		return;
	}
	
	@Before(MemberTokenInterceptor.class)
	@UploadAnnotation
	public void uploadFile(){

		UploadFile uploadFile = getFile();
		if(uploadFile == null){
			renderJson(getReturnJson(Code.ERROR, "请提供文件", EMPTY_OBJECT));
			return;
		}
		String webRoot = PathKit.getWebRootPath();
		String path = uploadFile.getUploadPath().substring(webRoot.length()) + File.separator + uploadFile.getFileName();
		
		BigInteger memberID = getParaToBigInteger("memberID");
		Attachment attachment = new Attachment();
		attachment.setUserId(memberID);
		attachment.setCreated(new Date());
		attachment.setTitle(uploadFile.getOriginalFileName());
		attachment.setPath(path);
		attachment.setSuffix(FileUtils.getSuffix(uploadFile.getFileName()));
		attachment.setMimeType(uploadFile.getContentType());
		attachment.save();

		JSONObject json = new JSONObject();
		json.put("src", path);
		renderJson(getReturnJson(Code.OK, "", json));
	}
	

	public void sendCaptcha(){
		String mobile = getPara("mobile");
		if(StrKit.isBlank(mobile)){
			renderJson(getReturnJson(Code.ERROR, "请提供手机号", EMPTY_OBJECT));
			return;
		}
		if(!isMobile(mobile)){
			renderJson(getReturnJson(Code.ERROR, "手机号格式错误", EMPTY_OBJECT));
			return;
		}
		
		String  code = (int) (Math.random() * 1000000) + "";
		JSONObject json = new JSONObject();
		json.put("code", code);
		try {
			sendSms(mobile, "SMS_86610162", json);
			Captcha captcha = getModel(Captcha.class);
			captcha.setCode(code);
			captcha.setMobile(mobile);
			captcha.setIp(this.getIPAddress());
			captcha.setCreateTime(new Date());
			captcha.save();
			renderJson(getReturnJson(Code.OK, "", EMPTY_OBJECT));
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			renderJson(getReturnJson(Code.ERROR, "短信发送失败", EMPTY_OBJECT));
		}
	}
	
	public void validCaptcha(){
		String mobile = getPara("mobile");
		if(StrKit.isBlank(mobile)){
			renderJson(getReturnJson(Code.ERROR, "请提供手机号", EMPTY_OBJECT));
			return;
		}
		if(!isMobile(mobile)){
			renderJson(getReturnJson(Code.ERROR, "手机号格式错误", EMPTY_OBJECT));
			return;
		}
		
		String captchaStr = getPara("captcha");
		if(StrKit.isBlank(captchaStr)){
			renderJson(getReturnJson(Code.ERROR, "请提供验证码", EMPTY_OBJECT));
			return;
		}
		
		Captcha captcha = CaptchaQuery.me().getCaptcha(mobile);
		if(captcha == null){
			renderJson(getReturnJson(Code.ERROR, "验证码不存在", EMPTY_OBJECT));
			return;
		}

		if(captchaStr.equals(captcha.getCode()) == false){
			renderJson(getReturnJson(Code.ERROR, "验证码错误", EMPTY_OBJECT));
			return;
		}
		
		long sendTime = captcha.getCreateTime().getTime();
		if(sendTime + 30 * 60 * 1000 < System.currentTimeMillis()){
			renderJson(getReturnJson(Code.ERROR, "验证码超时，请重新获取", EMPTY_OBJECT));
			return;
		}
		renderJson(getReturnJson(Code.OK, "", EMPTY_OBJECT));
		return;
	}
	
	@SuppressWarnings("unused")
	@Clear(AccessTokenInterceptor.class)
	public void getAccessToken(){
		if(DEBUG == false) return;
		renderJson(getReturnJson(Code.OK, AccessTokenInterceptor.getCurrentAccessToken(), EMPTY_OBJECT));
		return;
	}
	
	@SuppressWarnings("unused")
	@Clear(AccessTokenInterceptor.class)
	public void resetMemberToken(){
		if(DEBUG == false) return;
		String mobile = getPara("mobile");
		if(StrKit.isBlank(mobile)){
			renderJson(getReturnJson(Code.ERROR, "请提供手机号", EMPTY_OBJECT));
			return;
		}
		if(!isMobile(mobile)){
			renderJson(getReturnJson(Code.ERROR, "手机号格式错误", EMPTY_OBJECT));
			return;
		}
		User member = UserQuery.me().findUserByMobile(mobile);
		if(member == null) {
			renderJson(getReturnJson(Code.ERROR, "用户不存在", EMPTY_OBJECT));
			return;
		}
		String memberToken = getRandomString(32);
		member.setMemberToken(memberToken);
		member.update();
		renderJson(getReturnJson(Code.OK, memberToken, EMPTY_OBJECT));
		return;
	}
	
	@Clear(AccessTokenInterceptor.class)
	public void uploadFileTest(){
		User member = UserQuery.me().findById(BigInteger.ONE);
		this.renderHtml("<html><head></head><body>"+
							"<form action='/jpress-web/v1/uploadFile' method='post' enctype='multipart/form-data'>"+
				                 "<input type='text' name='accessToken' value='"+AccessTokenInterceptor.getCurrentAccessToken()+"'>" +
							     "<input type='text' name='memberToken' value='"+member.getMemberToken()+"'>" +
							     "<input type='text' name='memberID' value='1'>" +
				                 "<input type='file' name='file'>" +
							     "<input type='submit' value='submit'>"+
				            "</form>"
				          +"</body></html>");
	}
}
