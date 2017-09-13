package yjt.api.v1;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
import yjt.model.Ad;
import yjt.model.Apply;
import yjt.model.Captcha;
import yjt.model.Contract;
import yjt.model.Feedback;
import yjt.model.Follow;
import yjt.model.Message;
import yjt.model.Report;
import yjt.model.query.AdQuery;
import yjt.model.query.ApplyQuery;
import yjt.model.query.CaptchaQuery;
import yjt.model.query.ContractQuery;
import yjt.model.query.FollowQuery;
import yjt.model.query.MessageQuery;
import yjt.verify.IdcardVerify;
import yjt.verify.MobileVerify;
import yjt.api.v1.Interceptor.*;
import yjt.Utils;
import yjt.api.v1.Annotation.*;


@RouterMapping(url="/v1")
@Before(AccessTokenInterceptor.class)
@Clear(JI18nInterceptor.class)
public class IndexController extends ApiBaseController {
	
	private static final boolean DEBUG = true;
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "password",  must = true, type = ParamInterceptor.Type.STRING, chs = "密码")
	public void login(){
		String mobile = getPara("mobile");
		String password = getPara("password");

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
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "captcha", must = true, type = ParamInterceptor.Type.STRING, chs = "验证码")
	@ParamAnnotation(name = "password",must = true, type = ParamInterceptor.Type.STRING, chs = "密码", minlen=6)
	@ParamAnnotation(name = "avatar",  must = false, type = ParamInterceptor.Type.STRING, chs = "头像", def="")
	public void register(){
		String mobile = getPara("mobile");
		String captchaStr = getPara("captcha");

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
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "lat",  must = false, type = ParamInterceptor.Type.DOUBLE, min=1, max=90,  chs = "纬度")
	@ParamAnnotation(name = "lng",  must = false, type = ParamInterceptor.Type.DOUBLE, min=1, max=180, chs = "经度")
	public void index(){
		BigInteger memberID = getParaToBigInteger("memberID");
		String latStr = this.getPara("lat");
		String lngStr = this.getPara("lng");
		if(StrKit.notBlank(latStr, lngStr)){
			double lat = Double.valueOf(latStr);
			double lng = Double.valueOf(lngStr);
			User member = UserQuery.me().findById(memberID);
			member.setLat(lat);
			member.setLng(lng);
			member.update();
		}
		
		List<Ad> ads = AdQuery.me().findList();
		JSONObject[] adList = new JSONObject[ads.size()];
		for(int i = 0; i<ads.size(); i++){
			JSONObject json = new JSONObject();
			json.put("img", ads.get(i).getImg());
			json.put("url", ads.get(i).getUrl());
			adList[i] = json;
		}
		
		Apply.Status[] stats = {Apply.Status.VALID};
		List<Apply> applies = ApplyQuery.me().findList(null,null, stats, null, memberID);
		JSONObject[] applyList = new JSONObject[applies.size()];
		for(int i = 0; i<applies.size(); i++){
			applyList[i] = applies.get(i).getProfile();
		}
		
		JSONObject ret = new JSONObject();
		ret.put("adList", adList);
		ret.put("applyList", applyList);
		renderJson(getReturnJson(Code.OK, "", ret));
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "page",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "起始页")
	@ParamAnnotation(name = "pageSize",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "每页数")
	public void friendList() {
		BigInteger memberID = getParaToBigInteger("memberID");
		int page = getParaToInt("page");
		int pageSize = getParaToInt("pageSize");

		BigInteger[] followedAry =  FollowQuery.me().getFollowedList(memberID);
		int totalItems = followedAry.length;
		List<User> userAry = new ArrayList<User>();
		if(followedAry.length > 0 && followedAry.length > (page -1) * pageSize) {
			List<BigInteger> followedList = Arrays.asList(followedAry);
			int start = (page-1) * pageSize;
			int end = page * pageSize > followedAry.length ? followedAry.length : page * pageSize;
			List<BigInteger> subList = followedList.subList(start, end);
			followedAry = new BigInteger[subList.size()];
			subList.toArray(followedAry);
			userAry = UserQuery.me().findList(followedAry);
		} 
		
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		for(User u : userAry) {
			JSONObject o = new JSONObject();
			o.put("userID", u.getId().toString());
			o.put("userName", u.getRealname());
			o.put("userAvatar", u.getAvatar());
			o.put("isFollowed", "1");
			result.add(o);
		}
		
		JSONObject json = new JSONObject();
		json.put("totalItems", "" + totalItems);
		json.put("page", ""+page);
		json.put("pageSize", ""+pageSize);
		json.put("result", result);
		renderJson(getReturnJson(Code.OK, "", json));
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	public void userInfo(){
		BigInteger memberID = getParaToBigInteger("memberID");
		User member = UserQuery.me().findByIdNoCache(memberID);
		renderJson(getReturnJson(Code.OK, "", member.getMemberProfile()));
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "userID",  must = true, type = ParamInterceptor.Type.INT, min=1, chs = "用户")
	public void userDetail(){
		BigInteger memberID = getParaToBigInteger("memberID");
		BigInteger userID = getParaToBigInteger("userID");
		User user = UserQuery.me().findByIdNoCache(userID);
		if(user == null){
			renderJson(getReturnJson(Code.ERROR, "用户不存在", EMPTY_OBJECT));
			return;
		}
		Follow follow = FollowQuery.me().getFollow(userID, memberID);
		JSONObject profile = user.getUserProfile(true);
		profile.put("isFollowed", follow != null ? "1" : "0");
		
		JSONObject userContractStatics = ContractQuery.me().contractStatics(userID);
		JSONObject betweenContractStatics = ContractQuery.me().contractBetween(userID, memberID);
		JSONObject deal = new JSONObject();
		deal.putAll(userContractStatics);
		deal.putAll(betweenContractStatics);
		
		JSONObject ret = new JSONObject();
		ret.put("index", profile);
		ret.put("deal", deal);
		
		renderJson(getReturnJson(Code.OK, "", ret));
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "page",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "起始页")
	@ParamAnnotation(name = "pageSize",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "每页数")
	@ParamAnnotation(name = "type",  must = true, type = ParamInterceptor.Type.ENUM_STRING, allow_list= {"in","out","interest"},chs = "类型")
	public void userInOutList() {
		BigInteger memberID = getParaToBigInteger("memberID");
		int page = getParaToInt("page");
		int pageSize = getParaToInt("pageSize");
		String type = getPara("type");
		if(type.equals("interest")) type = "out";
		JSONObject ret = ContractQuery.me().inOutList(memberID, type, page, pageSize);
		renderJson(getReturnJson(Code.OK, "", ret));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "userID",  must = true, type = ParamInterceptor.Type.INT, chs = "关注对象")
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
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "gender",  must = false, type = ParamInterceptor.Type.INT, min=0, max=2, chs = "性别")
	@ParamAnnotation(name = "birthday",  must = false, type = ParamInterceptor.Type.DATE, min=0, max=2, chs = "出生年月")
	@ParamAnnotation(name = "sysPush",  must = false, type = ParamInterceptor.Type.INT, min=0, max=1, chs = "系统消息提醒")
	@ParamAnnotation(name = "salePush",  must = false, type = ParamInterceptor.Type.INT, min=0, max=1, chs = "交易消息提醒")
	@ParamAnnotation(name = "inPush",  must = false, type = ParamInterceptor.Type.INT, min=0, max=1, chs = "催收消息提醒")
	@ParamAnnotation(name = "outPush",  must = false, type = ParamInterceptor.Type.INT, min=0, max=1, chs = "借款订阅消息")
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
		if(StrKit.notBlank(gender))	user.setGender(gender);
		if(StrKit.notBlank(birthday)) user.setBirthday(Utils.getYmd(birthday));
		if(sysPush != null)  user.setSysPush(sysPush);
		if(salePush != null)  user.setSalePush(salePush);
		if(inPush != null)  user.setInPush(inPush);
		if(outPush != null)  user.setOutPush(outPush);

		boolean flag = user.update();
		if(flag){
			HashMap<String, Object> profile = user.getMemberProfile();
			renderJson(getReturnJson(Code.OK, "", profile));
		}else{
			renderJson(getReturnJson(Code.ERROR, "更新失败", EMPTY_OBJECT));
			return;
		}
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "mobile",  must = false, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "eachFollowed",  must = false, type = ParamInterceptor.Type.INT, min=0, max=2, chs = "关注参数")
	public void searchUser(){
		BigInteger memberID = getParaToBigInteger("memberID");
		User member = UserQuery.me().findByIdNoCache(memberID);

		String mobile = getPara("mobile");
		Integer eachFollowed = this.getParaToInt("eachFollowed");
		
		if(eachFollowed != null){
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
				JSONObject[] profiles = new JSONObject[num];
				for(int i = 0; i<num; i++){
					User u = list.get(i);
					JSONObject profile = u.getUserProfile(false);
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
			HashMap<String, Object> data = new HashMap<String, Object>();
			//以下需要包装成 {'result':[{'userID':'xxx'...},{'userID':'xxx'...}]}
			User user = UserQuery.me().findUserByMobile(mobile);
			if(user == null){
				data.put("result", EMPTY_ARRAY);
				renderJson(getReturnJson(Code.OK, "用户不存在", data));
				return;
			}
			JSONObject profile = user.getUserProfile(false);
			//是否已关注刚被搜索出来的对方
			Follow follow = FollowQuery.me().getFollow(user.getId(), member.getId());
			String flw = (follow != null && follow.getStatus() == Follow.Status.UNFOLLOWED.getIndex()) ? "1" : "0";
			profile.put("isFollowed", flw);
			JSONObject[] profiles = new JSONObject[1];
			profiles[0] = profile;
			data.put("result", profiles);
			renderJson(getReturnJson(Code.OK, "", data));
			return;
		} else {
			renderJson(getReturnJson(Code.ERROR, "无参数错误", EMPTY_OBJECT));
			return;
		}
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	public void validMobile(){
		renderJson(getReturnJson(Code.OK, "", EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	public void existsMobile(){
		String mobile = getPara("mobile");
		User user = UserQuery.me().findUserByMobile(mobile);
		Code code = (user != null) ? Code.OK : Code.ERROR;
		renderJson(getReturnJson(code, "", EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "oldPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=1,  chs = "旧密码")
	@ParamAnnotation(name = "newPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=6,  chs = "新密码")
	public void updatePwd(){
		BigInteger memberID = getParaToBigInteger("memberID");
		String oldPwd = getPara("oldPwd").trim();
		String newPwd = getPara("newPwd").trim();
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
		profile.put("memberToken", newMemberToken);
		renderJson(getReturnJson(Code.OK, "", profile));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "oldPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=0,  chs = "旧交易密码")
	@ParamAnnotation(name = "newPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=6,  chs = "新交易密码")
	public void updateDealPwd() {
		BigInteger memberID = getParaToBigInteger("memberID");
		String oldPwd = getPara("oldPwd");
		oldPwd = StrKit.notBlank(oldPwd) ? oldPwd : "";
		String newPwd = getPara("newPwd").trim();
		User member = UserQuery.me().findByIdNoCache(memberID);
		if(StrKit.notBlank(member.getDealPassword())){
			if(!EncryptUtils.verlifyUser(member.getDealPassword(), member.getDealSalt(), oldPwd)){
				renderJson(getReturnJson(Code.ERROR, "原交易密码错误", EMPTY_OBJECT));
				return;
			}
		}
		HashMap<String, Object> profile = member.getMemberProfile();
		
		String salt = EncryptUtils.salt();
		member.setDealPassword(EncryptUtils.encryptPassword(newPwd, salt));
		member.setDealSalt(salt);
		String newMemberToken = getRandomString(32);
		member.setMemberToken(newMemberToken);
		member.update();
		profile.put("memberToken", newMemberToken);
		renderJson(getReturnJson(Code.OK, "", profile));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
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
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "page",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "起始页")
	@ParamAnnotation(name = "pageSize",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "每页数")
	public void userMsgList(){
		BigInteger memberID = getParaToBigInteger("memberID");
		int page = getParaToInt("page");
		int pageSize = getParaToInt("pageSize");
		long totalItems = MessageQuery.me().findCount(null, memberID, null);
		List<Message> msgList = MessageQuery.me().findList(page, pageSize, null, memberID, null);
		
		JSONObject ret = new JSONObject();
		ret.put("page", ""+page);
		ret.put("pageSize", ""+pageSize);
		ret.put("totalItems", ""+totalItems);
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		ret.put("result", result);
		for(Message msg : msgList){
			JSONObject json = new JSONObject();
			json.put("id", msg.getId().toString());
			json.put("content", msg.getContent());
			Date createTime = msg.getCreateTime();
			json.put("createTime", createTime != null ? Utils.toYmdHms(msg.getCreateTime()) : "");
			json.put("type", ""+msg.getType());
			json.put("isRead", "" + msg.getIsRead());
			json.put("isAction", "" + msg.getIsAction());
			
			JSONObject action = new JSONObject();
			action.put("act", msg.getAct());
			BigInteger contractId =  msg.getContractId();
			action.put("contractID", contractId != null ? contractId.toString() : "");
			BigInteger applyId = msg.getApplyId();
			action.put("applyID", applyId != null ? applyId.toString() : "");
			action.put("url", msg.getUrl());
			json.put("action", action);
			
			result.add(json);
		}
		renderJson(getReturnJson(Code.OK, "", ret));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "money",  must = true, type = ParamInterceptor.Type.DOUBLE, min=1, max=100000000,chs = "借款金额")
	@ParamAnnotation(name = "endDate",  must = true, type = ParamInterceptor.Type.DATE, chs = "还款日期")
	@ParamAnnotation(name = "rate",  must = true, type = ParamInterceptor.Type.DOUBLE, min=1, max=24,chs = "年化利率")
	@ParamAnnotation(name = "forUseType",  must = true, type = ParamInterceptor.Type.STRING, minlen=1,chs = "借款用途")
	@ParamAnnotation(name = "toFriends",  must = true, type = ParamInterceptor.Type.STRING, minlen=1,chs = "借款人")
	@ParamAnnotation(name = "video",  must = false, type = ParamInterceptor.Type.STRING, minlen=1,chs = "视频")
	public void memberBorrow(){
		BigInteger memberID = getParaToBigInteger("memberID");
		String moneyStr = getPara("money");
		String endDateStr = getPara("endDate");
		String rate = getPara("rate");
		String forUseType = getPara("forUseType");
		String toFriendsStr = getPara("toFriends");
		String video = getPara("video");
		if(video == null) video = "";
		
		double amount = Double.parseDouble(moneyStr);
		if(amount >= 3000.0 && StrKit.isBlank(video)){
			renderJson(getReturnJson(Code.ERROR, "请提供视频", EMPTY_OBJECT));
			return;
		}
		Date maturity_date =Utils.getYmd(endDateStr);
		if(Utils.getTodayStartTime() + 86400 * 1000 > maturity_date.getTime()){
			renderJson(getReturnJson(Code.ERROR, "还款日太近", EMPTY_OBJECT));
			return;
		}
		double annual_rate = Double.parseDouble(rate);
		Apply.Purpose purpose = Apply.Purpose.getEnum(forUseType);
		if(purpose == null){
			renderJson(getReturnJson(Code.ERROR, "不能识别的借款用途", EMPTY_OBJECT));
			return;
		}
		
		ArrayList<BigInteger> toFriends = Utils.splitToBigInteger(toFriendsStr, ",", true);
		if(toFriends.size() == 0){
			renderJson(getReturnJson(Code.ERROR, "没有借款人", EMPTY_OBJECT));
			return;
		}
		List<User> u = UserQuery.me().findList(toFriends);
		if(u.size() != toFriends.size()){
			renderJson(getReturnJson(Code.ERROR, "有部分发布对象不存在", EMPTY_OBJECT));
			return;
		}
		
		//获取当前关注列表，以检查是否向非好友发布了借款信息
		BigInteger[] followedArray = FollowQuery.me().getFollowedList(memberID);
		List<BigInteger> followedList = Arrays.asList(followedArray);
		ArrayList<String> nonFriends = new ArrayList<String>();
		for(BigInteger bi : toFriends){
			if(followedList.contains(bi) == false){
				nonFriends.add(bi.toString());
			}
		}
		if(nonFriends.size() > 0){
			renderJson(getReturnJson(Code.ERROR, "不可向非好友发布借款信息", nonFriends));
			return;
		}
		
		Apply apply = getModel(Apply.class);
		apply.setAmount(amount);
		apply.setAnnualRate(annual_rate);
		apply.setApplyUid(memberID);
		apply.setCreateTime(new Date());
		apply.setDescription("");
		apply.setMaturityDate(maturity_date);
		apply.setPurpose(purpose.getIndex());
		apply.setStatus(Apply.Status.VALID.getIndex());
		apply.setToFriends(toFriends);
		apply.setVideo(video);
		apply.save();
		JSONObject json = new JSONObject();
		json.put("id", apply.getId().toString());
		renderJson(getReturnJson(Code.OK, "", json));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "applyID",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "申请号")
	public void applyDetail(){
		BigInteger applyID = getParaToBigInteger("applyID");
		Apply apply = ApplyQuery.me().findById(applyID);
		if(apply == null){
			renderJson(getReturnJson(Code.ERROR, "申请不存在", EMPTY_OBJECT));
			return;
		}
		renderJson(getReturnJson(Code.OK, "", apply.getProfile()));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "applyID",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "申请号")
	@ParamAnnotation(name = "dealPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=6,chs = "交易密码")
	@ParamAnnotation(name = "repaymentMethod",  must = false, type = ParamInterceptor.Type.INT, min=1, max=3,chs = "还款方式")
	public void payApply() {
		BigInteger memberID = getParaToBigInteger("memberID");
		User member = UserQuery.me().findByIdNoCache(memberID);
		String dealPwd = getPara("dealPwd").trim();
		if(!EncryptUtils.verlifyUser(member.getDealPassword(), member.getDealSalt(), dealPwd)){
			renderJson(getReturnJson(Code.ERROR, "交易密码错误", EMPTY_OBJECT));
			return;
		}
		
		BigInteger applyID = getParaToBigInteger("applyID");
		Apply apply = ApplyQuery.me().findById(applyID);
		if(apply == null){
			renderJson(getReturnJson(Code.ERROR, "申请不存在", EMPTY_OBJECT));
			return;
		}
		User debitor = UserQuery.me().findByIdNoCache(apply.getApplyUid());
		if(debitor == null) {
			renderJson(getReturnJson(Code.ERROR, "借款方不存在", EMPTY_OBJECT));
			return;
		}
		
		//该申请的状态是否可交易
		String canDeal = apply.canDeal();
		if(StrKit.notBlank(canDeal)) {
			renderJson(getReturnJson(Code.ERROR, canDeal, EMPTY_OBJECT));
			return;
		}
		
		if(member.getAmount().compareTo(apply.getAmount()) < 0) {
			renderJson(getReturnJson(Code.ERROR, "余额不足", EMPTY_OBJECT));
			return;
		}
		
		BigInteger userID = apply.getApplyUid();
		if(userID.equals(memberID)) {
			renderJson(getReturnJson(Code.ERROR, "不能向自己出借", EMPTY_OBJECT));
			return;
		}
		
		if(apply.isInFriendList(memberID) == false) {
			renderJson(getReturnJson(Code.ERROR, "您不是该申请的发布对象", EMPTY_OBJECT));
			return;
		}
		
		if(member.getCanLend() != 1) {
			renderJson(getReturnJson(Code.ERROR, "您没有借出的权限", EMPTY_OBJECT));
			return;
		}
		
		
		int repaymentMethod = getParaToInt("repaymentMethod", Contract.getDefaultReportMethod().getIndex());
		JSONObject result = Contract.createContract(apply, member, debitor, Contract.RepaymentMethod.getEnum(repaymentMethod));
		if(StrKit.notBlank(result.getString("err"))) {
			renderJson(getReturnJson(Code.ERROR, result.getString("err"), EMPTY_OBJECT));
			return;
		}
		renderJson(getReturnJson(Code.OK, "", apply.getProfile())); 
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "type",  must = true, type = ParamInterceptor.Type.ENUM_STRING, allow_list= {"reg","findPwd","validMobile"},chs = "类型")
	public void sendCaptcha(){
		String mobile = getPara("mobile");
		String code = String.format("%06d", (int) (Math.random() * 1000000));
		String type = getPara("type");
		String template = null;
		if(type.equals("reg")) template = "SMS_94665102";
		else if(type.equals("findPwd")) template = "SMS_94665102";
		else if(type.equals("validMobile"))  template = "SMS_94665102";
		else {
			return;
		}
		JSONObject json = new JSONObject();
		json.put("code", code);
		try {
			sendSms(mobile, template, json);
			Captcha captcha = getModel(Captcha.class);
			captcha.setCode(code);
			captcha.setType(type);
			captcha.setMobile(mobile);
			captcha.setIp(this.getIPAddress());
			captcha.setCreateTime(new Date());
			captcha.save();
			renderJson(getReturnJson(Code.OK, "", EMPTY_OBJECT));
		} catch (ClientException e) {
			renderJson(getReturnJson(Code.ERROR, "短信发送失败", EMPTY_OBJECT));
		}
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "captcha",  must = true, type = ParamInterceptor.Type.STRING, minlen=4, chs = "验证码")
	public void validCaptcha(){
		String mobile = getPara("mobile");
		String captchaStr = getPara("captcha");
		
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
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "messageID",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "消息")
	public void userMsgRead() {
		BigInteger messageID = getParaToBigInteger("messageID");
		Message message = MessageQuery.me().findById(messageID);
		if(message == null){
			renderJson(getReturnJson(Code.ERROR, "消息不存在", EMPTY_OBJECT));
			return;
		}
		BigInteger memberID = getParaToBigInteger("memberID");
		if(message.getToUserId().equals(memberID) == false) {
			renderJson(getReturnJson(Code.ERROR, "无权回执", EMPTY_OBJECT));
			return;
		}
		message.setIsRead(1);
		message.setReadTime(new Date());
		message.update();
		renderJson(getReturnJson(Code.OK, "", EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	public void userClearMsg() {
		BigInteger memberID = getParaToBigInteger("memberID");
		int count = MessageQuery.me().deleteAll(memberID);
		JSONObject json = new JSONObject();
		json.put("count", ""+count);
		renderJson(getReturnJson(Code.OK, "", json));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	public void badge() {
		BigInteger memberID = getParaToBigInteger("memberID");
		long msg = MessageQuery.me().findCount(null, memberID, false);
		JSONObject json = new JSONObject();
		json.put("msg", ""+msg);
		renderJson(getReturnJson(Code.OK, "", json));
		return;
	}

	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "type",  must = true, type = ParamInterceptor.Type.STRING, minlen=1, chs = "消息类型")
	public void clearBadge() {
		BigInteger memberID = getParaToBigInteger("memberID");
		int msg = 0;
		String type = getPara("type");
		if(type.equals("msg")) {
			msg = MessageQuery.me().readAll(memberID);
		}
		JSONObject json = new JSONObject();
		json.put("msg", ""+msg);
		renderJson(getReturnJson(Code.OK, "", json));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = false, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "name",  must = false, type = ParamInterceptor.Type.STRING, minlen=1, chs = "联系人")
	@ParamAnnotation(name = "tel",  must = false, type = ParamInterceptor.Type.STRING, minlen=1, chs = "联系电话")
	@ParamAnnotation(name = "content",  must = true, type = ParamInterceptor.Type.STRING, minlen=1, chs = "反馈内容")
	public void feedback() {
		BigInteger memberID = getParaToBigInteger("memberID", BigInteger.ZERO);
		//如果带有memberID的参数，则必须要提供此用户的令牌
		if(StrKit.isBlank(getPara("memberToken")) && !memberID.equals(BigInteger.ZERO)) {
			renderJson(getReturnJson(Code.ERROR, "请提供用户令牌", EMPTY_OBJECT));
			return;
		}
		String name = getPara("name", "");
		String tel = getPara("tel", "");
		String content = getPara("content");
		
		Feedback feedback = getModel(Feedback.class);
		feedback.setUserid(memberID);
		feedback.setName(name);
		feedback.setTel(tel);
		feedback.setContent(content);
		feedback.setCreateTime(new Date());
		boolean flag = feedback.save();
		if(flag) {
			JSONObject json = new JSONObject();
			json.put("feedbackID", feedback.getId().toString());
			renderJson(getReturnJson(Code.OK, "", json));
			return;
		} else {
			renderJson(getReturnJson(Code.ERROR, "新增反馈失败", EMPTY_OBJECT));
			return;
		}
		
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "content",  must = true, type = ParamInterceptor.Type.STRING, minlen=3, chs = "举报内容")
	@ParamAnnotation(name = "imgs",  must = false, type = ParamInterceptor.Type.STRING, chs = "举报图片")
	@ParamAnnotation(name = "toUserID",  must = false, type = ParamInterceptor.Type.INT, min=1, chs = "被举报人")
	public void report() {
		BigInteger memberID = getParaToBigInteger("memberID");
		String content = getPara("content");
		String imgs = getPara("imgs");
		imgs = StrKit.notBlank(imgs) ? imgs : "";
		
		String toUserIDStr = getPara("toUserID");
		BigInteger toUserID = StrKit.notBlank(toUserIDStr) ? BigInteger.valueOf(Long.valueOf(toUserIDStr)) : BigInteger.ZERO;
		if(toUserID.equals(BigInteger.ZERO) == false) {
			User user = UserQuery.me().findById(toUserID);
			if(user == null) {
				renderJson(getReturnJson(Code.ERROR, "被举报人不存在", EMPTY_OBJECT));
				return;
			}
		}
		
		Report report = getModel(Report.class);
		report.setFromUserId(memberID);
		report.setToUserId(toUserID);
		report.setImgs(imgs);
		report.setContent(content);
		report.setCreateTime(new Date());
		boolean flag = report.save();
		if(flag) {
			JSONObject json = new JSONObject();
			json.put("reportID", report.getId().toString());
			renderJson(getReturnJson(Code.OK, "", json));
			return;
		} else {
			renderJson(getReturnJson(Code.ERROR, "新增举报失败", EMPTY_OBJECT));
			return;
		}
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "applyID",  must = true, type = ParamInterceptor.Type.INT, min=1, chs = "申请号")
	public void applyAgreement(){
		BigInteger memberID = getParaToBigInteger("memberID");
		BigInteger applyID = getParaToBigInteger("applyID");
		Apply apply = ApplyQuery.me().findById(applyID);
		if(apply == null){
			renderJson(getReturnJson(Code.ERROR, "申请不存在", EMPTY_OBJECT));
			return;
		}
		Apply.Status apply_status = Apply.Status.getEnum(apply.getStatus());
		if(apply_status == Apply.Status.INVALID){
			renderJson(getReturnJson(Code.ERROR, "申请已无效", EMPTY_OBJECT));
			return;
		}
		if(apply.getMaturityDate().getTime() < Utils.getTodayStartTime()){
			renderJson(getReturnJson(Code.ERROR, "申请已过期", EMPTY_OBJECT));
			return;
		}
		User creditor = null;
		User debitor = apply.getApplyUser();
		Contract contract = null;
		Date creditorSign = null;
		BigInteger contractId = apply.getContractId();
		if(contractId != null && !contractId.equals(BigInteger.ZERO)) {
			contract = ContractQuery.me().findById(contractId);
			if(contract == null) {
				log.info("apply.id(" + applyID.toString() + ") 已经成交但无对应的合约记录");
				renderJson(getReturnJson(Code.ERROR, "内部错误", EMPTY_OBJECT));
				return;
			}
			creditor = contract.getCreditUser();
			if(memberID.equals(creditor.getId()) == false && memberID.equals(debitor.getId()) == false) {
				renderJson(getReturnJson(Code.ERROR, "您不是当事方无权查看", EMPTY_OBJECT));
				return;
			}
		} else {
			if(memberID.equals(debitor.getId()) == false) {
				creditorSign = new Date();
				creditor = UserQuery.me().findById(memberID);
			}
		}
		
		String agreement = Contract.getAgreementFilePath(contract, apply, debitor, creditor, apply.getCreateTime(), creditorSign);
		if(agreement == null) {
			renderJson(getReturnJson(Code.ERROR, "协议生成失败", EMPTY_OBJECT));
			return;
		}

		RenderFile render = new RenderFile();
		String mime = "image/" + Utils.getFileExtention(agreement);
		render.setContext(this.getRequest(), this.getResponse(), agreement, mime, (contract == null));
		this.render(render);
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "idcard",  must = true, type = ParamInterceptor.Type.STRING, minlen=18, maxlen=18, chs = "身份证号")
	@ParamAnnotation(name = "realname",  must = true, type = ParamInterceptor.Type.STRING, minlen=2, maxlen=4, chs = "真实姓名")
	@ParamAnnotation(name = "idcardimg1",  must = true, type = ParamInterceptor.Type.STRING, minlen=12, chs = "身份证正面照片")
	@ParamAnnotation(name = "idcardimg2",  must = true, type = ParamInterceptor.Type.STRING, minlen=12, chs = "身份证反面照片")
	public void verifyIDCard() {
		BigInteger memberID = getParaToBigInteger("memberID");
		String idcard = getPara("idcard").trim().toLowerCase();
		String realname = getPara("realname").trim().toLowerCase();
		String idcardimg1 = getPara("idcardimg1").trim();
		String idcardimg2 = getPara("idcardimg2").trim();
		
		User member = UserQuery.me().findByIdNoCache(memberID);
		if(member.getAuthCard() == 1) {
			renderJson(getReturnJson(Code.ERROR, "请勿重复认证", EMPTY_OBJECT));
			return;
		}
		
		//检测名字是否有英文
		if(Utils.hasAlphaBeta(realname)) {
			renderJson(getReturnJson(Code.ERROR, "请用真实姓名", EMPTY_OBJECT));
			return;
		}
		
		//检查身份证号码的编码是否合法
		if(!Utils.isValidIdcard(idcard)) {
			renderJson(getReturnJson(Code.ERROR, "身份证号码格式错误", EMPTY_OBJECT));
			return;
		}
		
		//开始验证
		boolean flag = IdcardVerify.verify(realname, idcard);
		if(flag == false) {
			renderJson(getReturnJson(Code.ERROR, "身份证认证失败", EMPTY_OBJECT));
			return;
		}
		
		member.setAuthCard(1);
		member.setIdcard(idcard);
		member.setRealname(realname);
		member.setIdcardFront(idcardimg1);
		member.setIdcardFront(idcardimg2);
		member.update();
		
		renderJson(getReturnJson(Code.OK, "身份证认证成功", EMPTY_OBJECT));
		return;
		
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "idcard",  must = false, type = ParamInterceptor.Type.STRING, minlen=18, maxlen=18, chs = "身份证号")
	@ParamAnnotation(name = "realname",  must = false, type = ParamInterceptor.Type.STRING, minlen=2, maxlen=4, chs = "真实姓名")
	@ParamAnnotation(name = "typeid",  must = false, type = ParamInterceptor.Type.INT, min=1, max=7, chs = "证件类型")
	public void verifyMobile(){
		BigInteger memberID = getParaToBigInteger("memberID");
		String idcard = getPara("idcard", "");
		String realname = getPara("realname", "");
		String mobile = getPara("mobile");
		Integer typeid = getParaToInt("typeid", 1);
		
		User member = UserQuery.me().findByIdNoCache(memberID);
		if(!mobile.equals(member.getMobile())){
			renderJson(getReturnJson(Code.ERROR, "该手机与您注册的手机号不同，如需修改请联系客服", EMPTY_OBJECT));
			return;
		}
		String mobileStatus = member.getMobileStatus();
		// 0:表示手机号码未验证; 1: 手机号码已验证但未实名认证； 2: 已通过验证和实名认证
		if("2".equals(mobileStatus)){
			renderJson(getReturnJson(Code.ERROR, "请勿重复认证", EMPTY_OBJECT));
			return;
		}
		if("0".equals(mobileStatus)){
			//一般不需要用到
			renderJson(getReturnJson(Code.ERROR, "请先验证手机号码", EMPTY_OBJECT));
			return;
		}
		
		int authCard = member.getAuthCard();
		if(authCard == 0){
			//renderJson(getReturnJson(Code.ERROR, "请先进行身份证认证", EMPTY_OBJECT));
			//return;
			if(!StrKit.notBlank(idcard, realname)){
				renderJson(getReturnJson(Code.ERROR, "请提供身份证号和真实姓名", EMPTY_OBJECT));
				return;
			}
			//检查身份证号码的编码是否合法
			if(!Utils.isValidIdcard(idcard)) {
				renderJson(getReturnJson(Code.ERROR, "身份证号码格式错误", EMPTY_OBJECT));
				return;
			}
			//检测名字是否有英文
			if(Utils.hasAlphaBeta(realname)) {
				renderJson(getReturnJson(Code.ERROR, "请用真实姓名", EMPTY_OBJECT));
				return;
			}
		} else {
			realname = member.getRealname();
			idcard = member.getIdcard();
		}
		
		//开始验证
		boolean flag = MobileVerify.verify(idcard,mobile,  realname, typeid.toString());
		if(flag == false) {
			renderJson(getReturnJson(Code.ERROR, "手机号码认证失败", EMPTY_OBJECT));
			return;
		}
		
		//如该用户未进行过身份证认证，顺便把身份证也一并设为认证了(安全性?)
		if(authCard == 0){
			member.setIdcard(idcard);
			member.setRealname(realname);
			member.setAuthCard(1);
		}
		member.setMobileStatus("2");
		member.update();
		
		renderJson(getReturnJson(Code.OK, "手机号码认证成功", EMPTY_OBJECT));
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
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	public void resetMemberToken(){
		if(DEBUG == false) return;
		String mobile = getPara("mobile");
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
