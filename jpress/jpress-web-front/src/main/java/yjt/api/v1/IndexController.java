package yjt.api.v1;

import java.io.File;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.upload.UploadFile;

import io.jpress.core.interceptor.JI18nInterceptor;
import io.jpress.model.Attachment;
import io.jpress.model.Option;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.EncryptUtils;
import io.jpress.utils.FileUtils;
import yjt.model.Ad;
import yjt.model.Apply;
import yjt.model.Captcha;
import yjt.model.Contract;
import yjt.model.CreditLog;
import yjt.model.Feedback;
import yjt.model.Follow;
import yjt.model.Message;
import yjt.model.Report;
import yjt.model.UnionpayLog;
import yjt.model.Withdraw;
import yjt.model.query.AdQuery;
import yjt.model.query.ApplyQuery;
import yjt.model.query.CaptchaQuery;
import yjt.model.query.ContractQuery;
import yjt.model.query.CreditLogQuery;
import yjt.model.query.FollowQuery;
import yjt.model.query.MessageQuery;
import yjt.model.query.UnionpayLogQuery;
import yjt.verify.BankcardDetail;
import yjt.verify.BankcardVerify;
import yjt.verify.IdcardVerify;
import yjt.verify.MobileVerify;
import yjt.api.v1.Interceptor.*;
import yjt.api.v1.Push.BalanceAddPush;
import yjt.api.v1.Push.LendoutPush;
import yjt.api.v1.UnionAppPay.UnionAppPay;
import yjt.api.v1.UnionAppPay.UnionAppPayMethod;
import yjt.core.push.Push;
import yjt.AliSms;
import yjt.Utils;
import yjt.YuxinSms;
import yjt.api.v1.Annotation.*;


@RouterMapping(url="/v1")
@Before(AccessTokenInterceptor.class)
@Clear(JI18nInterceptor.class)
public class IndexController extends ApiBaseController {
	protected static final Log log = Log.getLog(IndexController.class);
	
	private static final boolean DEBUG = true;
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号", mustErrTips="手机号或密码错误", typeErrTips="手机号格式错误")
	@ParamAnnotation(name = "password",  must = true, type = ParamInterceptor.Type.STRING, chs = "密码", mustErrTips="手机号或密码错误")
	public void login(){
		String mobile = getPara("mobile");
		String password = getPara("password");
		User user = UserQuery.me().findUserByMobile(mobile);
		if(user == null){
			renderJson(getReturnJson(Code.ERROR, "手机号或密码错误", EMPTY_OBJECT));
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
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号", typeErrTips="手机号格式错误")
	@ParamAnnotation(name = "captcha", must = true, type = ParamInterceptor.Type.STRING, chs = "验证码")
	@ParamAnnotation(name = "password",must = true, type = ParamInterceptor.Type.STRING, chs = "密码", minlen=6, minlenErrTips="密码至少六位")
	@ParamAnnotation(name = "avatar",  must = false, type = ParamInterceptor.Type.STRING, chs = "头像", minlen=12, minlenErrTips="头像错误")
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
		//通过手机号注册的帐号, mobile_status为0，待认证后改成1
		user.setMobileStatus("0");
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
		renderJson(getReturnJson(Code.OK, "注册成功", profile));
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	public void index(){
		BigInteger memberID = getParaToBigInteger("memberID");		
		List<Ad> ads = AdQuery.me().findList();
		JSONObject[] adList = new JSONObject[ads.size()];
		for(int i = 0; i<ads.size(); i++){
			JSONObject json = new JSONObject();
			json.put("img", Utils.toMedia(ads.get(i).getImg()));
			json.put("url", Utils.toMedia(ads.get(i).getUrl()));
			adList[i] = json;
		}
		
		Apply.Status[] stats = {Apply.Status.VALID};
		List<Apply> applies = ApplyQuery.me().findList(null,null, stats, null, memberID, 1);
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
			o.put("userAvatar", Utils.toMedia(u.getAvatar()));
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
	@ParamAnnotation(name = "userID",  must = true, type = ParamInterceptor.Type.INT, min=1, chs = "用户", minErrTips="用户不存在")
	public void userDetail(){
		BigInteger memberID = getParaToBigInteger("memberID");
		BigInteger userID = getParaToBigInteger("userID");
		User user = UserQuery.me().findByIdNoCache(userID);
		if(user == null){
			renderJson(getReturnJson(Code.ERROR, "用户不存在", EMPTY_OBJECT));
			return;
		}
		JSONObject profile = user.getUserProfile(true);
		Follow follow = FollowQuery.me().getFollow(userID, memberID);
		
		String flw = (follow != null && follow.getStatus() == Follow.Status.FOLLOWED.getIndex()) ? "1" : "0";
		profile.put("isFollowed", flw);
		
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
	@ParamAnnotation(name = "userID",  must = true, type = ParamInterceptor.Type.INT, min=1, chs = "关注对象", minErrTips="用户不存在")
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
		profile.put("userAvatar", Utils.toMedia(user.getAvatar()));
		profile.put("userMobile", user.getMobile());
		profile.put("isFollowed", flag ? "1" : "0");
		profile.put("userLocation", user.getUserLocation());
		renderJson(getReturnJson(Code.OK, "", profile));
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "gender",  must = false, type = ParamInterceptor.Type.INT, min=0, max=2, chs = "性别", minErrTips="性别错误", maxErrTips="性别错误")
	@ParamAnnotation(name = "birthday",  must = false, type = ParamInterceptor.Type.DATE, chs = "出生年月")
	@ParamAnnotation(name = "sysPush",  must = false, type = ParamInterceptor.Type.INT, min=0, max=1, chs = "系统消息提醒", minErrTips="系统消息提醒错误", maxErrTips="系统消息提醒错误")
	@ParamAnnotation(name = "salePush",  must = false, type = ParamInterceptor.Type.INT, min=0, max=1, chs = "交易消息提醒", minErrTips="交易消息提醒错误", maxErrTips="交易消息提醒错误")
	@ParamAnnotation(name = "inPush",  must = false, type = ParamInterceptor.Type.INT, min=0, max=1, chs = "催收消息提醒", minErrTips="催收消息提醒错误", maxErrTips="催收消息提醒错误")
	@ParamAnnotation(name = "outPush",  must = false, type = ParamInterceptor.Type.INT, min=0, max=1, chs = "借款订阅消息", minErrTips="借款订阅消息错误", maxErrTips="借款订阅消息错误")
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
		if(StrKit.notBlank(avatar))	user.setAvatar(Utils.stripMedia(avatar));
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
			renderJson(getReturnJson(Code.OK, "已更新", profile));
		}else{
			renderJson(getReturnJson(Code.ERROR, "更新失败", EMPTY_OBJECT));
			return;
		}
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "mobile",  must = false, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "eachFollowed",  must = false, type = ParamInterceptor.Type.INT, min=0, max=2, chs = "关注参数", minErrTips="无效的关注选项", maxErrTips="无效的关注选项")
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
				renderJson(getReturnJson(Code.ERROR, "用户不存在", data));
				return;
			}
			JSONObject profile = user.getUserProfile(false);
			//是否已关注刚被搜索出来的对方
			Follow follow = FollowQuery.me().getFollow(user.getId(), member.getId());
			String flw = (follow != null && follow.getStatus() == Follow.Status.FOLLOWED.getIndex()) ? "1" : "0";
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
		renderJson(getReturnJson(Code.OK, "手机号格式正确", EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	public void existsMobile(){
		String mobile = getPara("mobile");
		User user = UserQuery.me().findUserByMobile(mobile);
		Code code =  Code.ERROR;
		String msg = "手机号不存在";
		if(user != null) {
			code = Code.OK;
			msg = "手机号已存在";
		}
		renderJson(getReturnJson(code, msg, EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "oldPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=1,  chs = "旧密码", minlenErrTips="旧密码过短")
	@ParamAnnotation(name = "newPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=6,  chs = "新密码", minlenErrTips="新密码不少于六位")
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
		renderJson(getReturnJson(Code.OK, "密码设置成功", profile));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "oldPwd",  must = false, type = ParamInterceptor.Type.STRING, minlen=1,  chs = "旧密码", minlenErrTips="旧密码过短")
	@ParamAnnotation(name = "newPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=6,  chs = "新密码", minlenErrTips="新密码不少于六位")
	public void updateLoginPwd(){
		BigInteger memberID = getParaToBigInteger("memberID");
		String oldPwd = getPara("oldPwd").trim();
		String newPwd = getPara("newPwd").trim();
		User member = UserQuery.me().findByIdNoCache(memberID);
		if(StrKit.notBlank(member.getPassword()) &&  !EncryptUtils.verlifyUser(member.getPassword(), member.getSalt(), oldPwd)){
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
		renderJson(getReturnJson(Code.OK, "密码设置成功", profile));
		return;
	}
	
	
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "oldPwd",  must = false, type = ParamInterceptor.Type.STRING, minlen=0,  chs = "旧交易密码")
	@ParamAnnotation(name = "newPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=6,  chs = "新交易密码", minlenErrTips="新交易密码至少六位")
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
		renderJson(getReturnJson(Code.OK, "交易密码设置成功", profile));
		return;
	}
	
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "captcha",  must = true, type = ParamInterceptor.Type.STRING, minlen=6, chs = "验证码", minlenErrTips="验证码为6位")
	public void updateMobile(){
		BigInteger memberID = getParaToBigInteger("memberID");
		User member = UserQuery.me().findByIdNoCache(memberID);
		String mobile = getPara("mobile");
		String captchaStr = getPara("captcha");

		String oldMobile = member.getMobile();
		if(mobile.equals(oldMobile)){
			renderJson(getReturnJson(Code.ERROR, "手机号与原手机号一样", EMPTY_OBJECT));
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
		
		//String newMemberToken = getRandomString(32);
		//member.setMemberToken(newMemberToken);
		member.setMobile(mobile);
		member.update();
		
		JSONObject json = new JSONObject();
		//json.put("memberToken", newMemberToken);
		json.put("mobile", mobile);
		
		renderJson(getReturnJson(Code.OK, "更换手机号成功", json));
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "captcha",  must = true, type = ParamInterceptor.Type.STRING, minlen=4, chs = "验证码", minlenErrTips="验证码至少四位")
	@ParamAnnotation(name = "newPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=6,  chs = "新密码", minlenErrTips="新密码至少六位")
	public void findPwd(){
		String mobile = getPara("mobile");
		String captchaStr = getPara("captcha");
		String newPwd = getPara("newPwd");
		
		User member = UserQuery.me().findUserByMobile(mobile);
		if(member == null){
			renderJson(getReturnJson(Code.ERROR, "用户不存在，请检查手机号", EMPTY_OBJECT));
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
		
		HashMap<String, Object> profile = member.getMemberProfile();
		
		String salt = EncryptUtils.salt();
		member.setPassword(EncryptUtils.encryptPassword(newPwd, salt));
		member.setSalt(salt);
		String newMemberToken = getRandomString(32);
		member.setMemberToken(newMemberToken);
		member.update();
		profile.put("memberToken", newMemberToken);
		renderJson(getReturnJson(Code.OK, "找回密码成功", profile));
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
		json.put("url", path);
		renderJson(getReturnJson(Code.OK, "文件上传成功", json));
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
	@ParamAnnotation(name = "money",  must = true, type = ParamInterceptor.Type.DOUBLE, mind=1, maxd=100000000,chs = "借款金额", minErrTips="借款金额至少1元", maxErrTips="需要借这么多钱么")
	@ParamAnnotation(name = "startDate",  must = false, type = ParamInterceptor.Type.DATE, chs = "起息日期")
	@ParamAnnotation(name = "endDate",  must = true, type = ParamInterceptor.Type.DATE, chs = "还款日期")
	@ParamAnnotation(name = "rate",  must = true, type = ParamInterceptor.Type.DOUBLE, mind=1.0, maxd=24.0,chs = "年化利率", minErrTips="借款年化利率不小于1%", maxErrTips="借款年化利率不高于24%")
	public void calcBorrow(){
		String moneyStr = getPara("money");
		String startDateStr = getPara("startDate");
		String endDateStr = getPara("endDate");
		String rate = getPara("rate");
		
		Date valueDate = null;
		if(StrKit.isBlank(startDateStr)){
			valueDate = new Date();
		} else {
			valueDate = Utils.getYmd(startDateStr);
		}
		
		double amount = Double.parseDouble(moneyStr);
		double annual_rate = Double.parseDouble(rate);
		Date maturity_date =Utils.getYmd(endDateStr);
		
		if(Utils.getTodayStartTime() + 1L * 86400 * 1000 > maturity_date.getTime()){
			renderJson(getReturnJson(Code.ERROR, "还款日期至少在1天之后", EMPTY_OBJECT));
			return;
		}

		if(Utils.getTodayStartTime() + 365L * 86400 * 1000 < maturity_date.getTime()){
			renderJson(getReturnJson(Code.ERROR, "借款周期不得超过1年", EMPTY_OBJECT));
			return;
		}
		
		Contract contract = new Contract();
		contract.setValueDate(valueDate);
		contract.setMaturityDate(maturity_date);
		contract.setAmount(amount);
		contract.setAnnualRate(annual_rate);
		contract.setStatus(Contract.Status.ESTABLISH.getIndex());
		double interest = contract.calcInterest();
		JSONObject json = new JSONObject();
		json.put("money", String.format("%.2f", interest + amount));
		renderJson(getReturnJson(Code.OK, "", json));
		return;

	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "money",  must = true, type = ParamInterceptor.Type.DOUBLE, mind=1, maxd=100000000,chs = "借款金额", minErrTips="借款金额至少1元", maxErrTips="需要借这么多钱么")
	@ParamAnnotation(name = "endDate",  must = true, type = ParamInterceptor.Type.DATE, chs = "还款日期")
	@ParamAnnotation(name = "rate",  must = true, type = ParamInterceptor.Type.DOUBLE, mind=1.0, maxd=24.0,chs = "年化利率", minErrTips="借款年化利率不小于1%", maxErrTips="借款年化利率不高于24%")
	@ParamAnnotation(name = "forUseType",  must = true, type = ParamInterceptor.Type.STRING, minlen=1,chs = "借款用途", minlenErrTips="借款用途错误")
	@ParamAnnotation(name = "toFriends",  must = true, type = ParamInterceptor.Type.STRING, minlen=1,chs = "发布对象", minlenErrTips="发布对象错误")
	@ParamAnnotation(name = "video",  must = false, type = ParamInterceptor.Type.STRING, minlen=12,chs = "视频", minlenErrTips="视频错误")
	public void memberBorrow(){
		BigInteger memberID = getParaToBigInteger("memberID");
		User member = UserQuery.me().findByIdNoCache(memberID);
		String moneyStr = getPara("money");
		String endDateStr = getPara("endDate");
		String rate = getPara("rate");
		String forUseType = getPara("forUseType");
		String toFriendsStr = getPara("toFriends");
		String video = getPara("video");
		if(video == null) video = "";
		else video = Utils.stripMedia(video);
		
		int authCard = member.getAuthCard();
		if(authCard == 0 || authCard == 2) {
			renderJson(getReturnJson(Code.ERROR, authCard == 0 ? "您没有进行名实认证，无法借款" : "实名认证中，待通过后方可借款", EMPTY_OBJECT));
			return;
		}
		
		String authMobile = member.getMobileStatusLogic();
		if("0".equals(authMobile) || "2".equals(authMobile)) {
			renderJson(getReturnJson(Code.ERROR, "0".equals(authMobile) ? "您没有进行手机号认证，无法借款" : "手机号认证中，待通过后方可借款", EMPTY_OBJECT));
			return;
		}
		
		int authBank = member.getAuthBank();
		if(authBank == 0 || authBank == 2) {
			renderJson(getReturnJson(Code.ERROR, authBank == 0 ? "您没有进行银行卡认证，无法借款" : "银行卡认证中，待通过后方可借款", EMPTY_OBJECT));
			return;
		}
		
		/*int authFace = member.getAuthFace();
		if(authFace == 0 || authFace == 2) {
			renderJson(getReturnJson(Code.ERROR, authFace == 0 ? "您没有进行人脸认证，无法借款" : "人脸认证中，待通过后方可借款", EMPTY_OBJECT));
			return;
		}*/
		
		double amount = Double.parseDouble(moneyStr);
		if(amount > member.getCanBorrowMoney() || ApplyQuery.me().checkExceedCredit(member, amount)) {
			renderJson(getReturnJson(Code.ERROR, "超出了您的借款额度 " + member.getCanBorrowMoney() + " 元", EMPTY_OBJECT));
			return;
		}
				
		int amountInt = (int)amount;
		if(amountInt % 10 != 0){
			renderJson(getReturnJson(Code.ERROR, "借款金额应是10的倍数", EMPTY_OBJECT));
			return;
		}
		
		if(amount >= 3000.0 && StrKit.isBlank(video)){
			renderJson(getReturnJson(Code.ERROR, "请提供视频", EMPTY_OBJECT));
			return;
		}
		Date maturity_date =Utils.getYmd(endDateStr);
		if(Utils.getTodayStartTime() + 1L * 86400 * 1000 > maturity_date.getTime()){
			renderJson(getReturnJson(Code.ERROR, "还款日期到少在1天之后", EMPTY_OBJECT));
			return;
		}
		if(Utils.getTodayStartTime() + 365L * 86400 * 1000 < maturity_date.getTime()){
			renderJson(getReturnJson(Code.ERROR, "借款周期不得超过1年", EMPTY_OBJECT));
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
		renderJson(getReturnJson(Code.OK, "借款申请发布成功", json));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "applyID",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "申请号", minErrTips="借款申请不存在")
	public void applyDetail(){
		BigInteger applyID = getParaToBigInteger("applyID");
		Apply apply = ApplyQuery.me().findById(applyID);
		if(apply == null){
			renderJson(getReturnJson(Code.ERROR, "借款申请不存在", EMPTY_OBJECT));
			return;
		}
		renderJson(getReturnJson(Code.OK, "", apply.getProfile()));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "applyID",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "申请号", minErrTips="申请号不存在")
	@ParamAnnotation(name = "dealPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=1,chs = "交易密码", minlenErrTips="交易密码错误")
	@ParamAnnotation(name = "repaymentMethod",  must = false, type = ParamInterceptor.Type.INT, min=1, max=3,chs = "还款方式", minErrTips="无法识别的还款方式", maxErrTips="无法识别的还款方式")
	public void payApply() {
		BigInteger memberID = getParaToBigInteger("memberID");
		User member = UserQuery.me().findByIdNoCache(memberID);
		String dealPwd = getPara("dealPwd", "");
		if(StrKit.notBlank(member.getDealPassword())){
			if(StrKit.isBlank(dealPwd)){
				renderJson(getReturnJson(Code.ERROR, "交易密码不能为空", EMPTY_OBJECT));
				return;
			}
			if(!EncryptUtils.verlifyUser(member.getDealPassword(), member.getDealSalt(), dealPwd)){
				renderJson(getReturnJson(Code.ERROR, "交易密码错误", EMPTY_OBJECT));
				return;
			}
		} else {
			renderJson(getReturnJson(Code.ERROR, "请先设置交易密码", EMPTY_OBJECT));
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
			renderJson(getReturnJson(Code.TIMEOUT, "余额不足", EMPTY_OBJECT));
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
		
		int authCard = member.getAuthCard();
		if(authCard == 0 || authCard == 2) {
			renderJson(getReturnJson(Code.ERROR, authCard == 0 ? "您没有进行实名认证，无法借出" : "实名认证中，待通过后方可借出", EMPTY_OBJECT));
			return;
		}
		
		String authMobile = member.getMobileStatusLogic();
		if("0".equals(authMobile) || "2".equals(authMobile)) {
			renderJson(getReturnJson(Code.ERROR, "0".equals(authMobile) ? "您没有进行手机号认证，无法借出" : "手机号认证中，待通过后方可借出", EMPTY_OBJECT));
			return;
		}
		
		int authBank = member.getAuthBank();
		if(authBank == 0 || authBank == 2) {
			renderJson(getReturnJson(Code.ERROR, authBank == 0 ? "您没有进行银行卡认证，无法借出" : "银行卡认证中，待通过后方可借出", EMPTY_OBJECT));
			return;
		}
		
		/*
		int authFace = member.getAuthFace();
		if(authFace == 0 || authFace == 2) {
			renderJson(getReturnJson(Code.ERROR, authFace == 0 ? "您没有进行人脸认证，无法借出" : "人脸认证中，待通过后方可借出", EMPTY_OBJECT));
			return;
		}
		*/
		
		int repaymentMethod = getParaToInt("repaymentMethod", Contract.getDefaultReportMethod().getIndex());
		JSONObject result = Contract.createContract(apply, member, debitor, Contract.RepaymentMethod.getEnum(repaymentMethod));
		if(StrKit.notBlank(result.getString("err"))) {
			renderJson(getReturnJson(Code.ERROR, result.getString("err"), EMPTY_OBJECT));
			return;
		}
		
		//发送推送
		String debitorDeviceToken = debitor.getDeviceToken();
		if(StrKit.notBlank(debitorDeviceToken)) {
			BalanceAddPush bap = new BalanceAddPush(debitorDeviceToken, member.getRealname(), apply.getAmount().doubleValue());
			Push.send(bap);
		}
		String creditorDeviceToken = member.getDeviceToken();
		if(StrKit.notBlank(creditorDeviceToken)) {
			LendoutPush lop = new LendoutPush(creditorDeviceToken, debitor.getRealname(), apply.getAmount().doubleValue());
			Push.send(lop);
		}
		
		renderJson(getReturnJson(Code.OK, "交易已成达", apply.getProfile())); 
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "applyID",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "申请号", minErrTips="申请号不存在")
	@ParamAnnotation(name = "dealPwd",  must = true, type = ParamInterceptor.Type.STRING, minlen=1,chs = "交易密码", minlenErrTips="交易密码错误")
	public void repayApply() {
		BigInteger memberID = getParaToBigInteger("memberID");
		User member = UserQuery.me().findByIdNoCache(memberID);
		String dealPwd = getPara("dealPwd", "");
		if(StrKit.notBlank(member.getDealPassword())){
			if(StrKit.isBlank(dealPwd)){
				renderJson(getReturnJson(Code.ERROR, "交易密码不能为空", EMPTY_OBJECT));
				return;
			}
			if(!EncryptUtils.verlifyUser(member.getDealPassword(), member.getDealSalt(), dealPwd)){
				renderJson(getReturnJson(Code.ERROR, "交易密码错误", EMPTY_OBJECT));
				return;
			}
		} else {
			renderJson(getReturnJson(Code.ERROR, "请先设置交易密码", EMPTY_OBJECT));
			return;
		}
		
		
		BigInteger applyID = getParaToBigInteger("applyID");
		
		Apply apply = ApplyQuery.me().findById(applyID);
		if(apply == null){
			renderJson(getReturnJson(Code.ERROR, "交易不存在", EMPTY_OBJECT));
			return;
		}
		Contract contract = ContractQuery.me().findById(apply.getContractId());
		if(contract == null) {
			renderJson(getReturnJson(Code.ERROR, "交易不存在", EMPTY_OBJECT));
			return;
		}
		
		
		if(Contract.Status.getEnum(contract.getStatus()) == Contract.Status.FINISH) {
			renderJson(getReturnJson(Code.ERROR, "已经还款，不要重复还款", EMPTY_OBJECT));
			return;
		}
		
		
		if(memberID.equals(apply.getApplyUid()) == false) {
			renderJson(getReturnJson(Code.ERROR, "您不是借款方，无权操作", EMPTY_OBJECT));
			return;
		}
		
		double change = contract.getAmount().doubleValue() + contract.calcInterest();
		log.info("apply.id" + applyID.toString());
		log.info("contract.id" + contract.getId().toString());
		log.info("contract.getAmount().doubleValue() = " + contract.getAmount().doubleValue());
		log.info("contract.calcInterest() = " + contract.calcInterest());
		log.info("member.getAmount().doubleValue() " + member.getAmount().doubleValue());
		
		if(member.getAmount().doubleValue() - change  < 0) {
			renderJson(getReturnJson(Code.ERROR, "余额不足，请充值", EMPTY_OBJECT));
			return;
		}
		
		boolean flag = member.changeBalance(-change, "主动还款", BigInteger.ZERO, CreditLog.Platfrom.JIETIAO365);
		if(flag == false) {
			renderJson(getReturnJson(Code.ERROR, "还款失败", EMPTY_OBJECT));
			log.error("用户 ("+memberID.toString()+") 主动还款失败(余额充足)。合约号：" + contract.getContractNumber());
			return;
		}
		contract.setStatus(Contract.Status.FINISH.getIndex());
		contract.setUpdateTime(new Date());
		contract.update();
		
		User creditor = contract.getCreditUser();
		if(creditor != null) {
			flag = creditor.changeBalance(change, "收到还款", memberID, CreditLog.Platfrom.JIETIAO365);
			log.error("用户 ("+creditor.getId().toString()+") 收款失败。合约号：" + contract.getContractNumber());
		}
		
		//发送推送
		String creditorDeviceToken = creditor.getDeviceToken();
		if(StrKit.notBlank(creditorDeviceToken)) {
			BalanceAddPush bap = new BalanceAddPush(creditorDeviceToken, member.getRealname(), change);
			Push.send(bap);
		}
		String debitorDeviceToken = member.getDeviceToken();
		if(StrKit.notBlank(debitorDeviceToken)) {
			LendoutPush lop = new LendoutPush(debitorDeviceToken, creditor.getRealname(), change);
			Push.send(lop);
		}
		
		renderJson(getReturnJson(Code.OK, "还款成功", apply.getProfile()));
		return;
	}
	
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "page",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "起始页")
	@ParamAnnotation(name = "pageSize",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "每页数")
	public void userBalanceInOutList() {
		BigInteger memberID = getParaToBigInteger("memberID");
		int page = getParaToInt("page");
		int pageSize = getParaToInt("pageSize");
		
		long totolItems = CreditLogQuery.me().findCount(memberID);
		List<CreditLog> list = new ArrayList<CreditLog>();
		if(totolItems > 0) {
			list = CreditLogQuery.me().findList(page, pageSize, memberID, null);
		}
		JSONObject[] result = new JSONObject[list.size()];
		for(int i = 0; i<list.size(); i++) {
			CreditLog log = list.get(i);
			JSONObject j = new JSONObject();
			j.put("id", log.getId().toString());
			j.put("money", String.format("%.2f", log.getChange()));
			j.put("info", log.getLog());
			j.put("type", CreditLog.Platfrom.getEnum(log.getPlatform()).getName());
			j.put("date", Utils.toYmd(log.getCreateTime()));
			result[i] = j;
		}
		
		JSONObject json = new JSONObject();
		json.put("page", "" + page);
		json.put("pageSize", "" + pageSize);
		json.put("totalItems", "" + totolItems);
		json.put("result", result);
		
		renderJson(getReturnJson(Code.OK, "", json));
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "type",  must = true, type = ParamInterceptor.Type.ENUM_STRING, allow_list= {"bank","weixin","alipay"},chs = "提现类型")
	@ParamAnnotation(name = "account",  must = true, type = ParamInterceptor.Type.STRING, minlen=12, maxlen=30, chs = "银行卡号", minlenErrTips="银行卡号错误", maxlenErrTips="银行卡号错误")
	@ParamAnnotation(name = "name",  must = true, type = ParamInterceptor.Type.STRING, minlen=2, maxlen=4, chs = "真实姓名", minlenErrTips="姓名不少于两个汉字", maxlenErrTips="姓名不超过四个汉字")
	@ParamAnnotation(name = "money",  must = true, type = ParamInterceptor.Type.DOUBLE, mind=10.0,chs = "提现金额", minErrTips="提现金额不少于10元")
	public void userWithdraw() {
		BigInteger memberID = getParaToBigInteger("memberID");
		String type = getPara("type");
		String account = getPara("account");
		String realname = getPara("name");
		String moneyStr = getPara("money");
		double money =  Double.parseDouble(moneyStr);
		
		
		User member = UserQuery.me().findByIdNoCache(memberID);
		if(!realname.equals(member.getRealname())) {
			renderJson(getReturnJson(Code.ERROR, "提交的真实姓名与本帐号注册的姓名不符，请联系客服修改", EMPTY_OBJECT));
			return;
		}
		//银行卡号就不检测了
		int balance = member.getAmount().intValue();
		if(balance < money) {
			renderJson(getReturnJson(Code.ERROR, "余额不足", EMPTY_OBJECT));
			return;
		}
		
		if(! "bank".equals(type)) {
			if("weixin".equals(type)) {
				renderJson(getReturnJson(Code.ERROR, "暂不支持微信提现", EMPTY_OBJECT));
			} else if("alipay".equals(type)) {
				renderJson(getReturnJson(Code.ERROR, "暂不支持支付宝提现", EMPTY_OBJECT));
			} else {
				renderJson(getReturnJson(Code.ERROR, "不能识别的提现方式", EMPTY_OBJECT));
			}
			return;
		}
		
		Withdraw withdraw = getModel(Withdraw.class);
		withdraw.setUserId(memberID);
		withdraw.setType(Withdraw.PayType.UNIONPAY.getIndex());  //默认银联支付
		withdraw.setMoney(money);
		withdraw.setBankAccount(account);
		withdraw.setRealname(realname);
		withdraw.setCreateTime(new Date());
		withdraw.setStatus(0);
		boolean flag = withdraw.save();
		if(flag == false) {
			log.error("<<<<<<<<<<<<<<<提现申请无法插入数据库>>>>>>>>>>>>>>>>>>");
			renderJson(getReturnJson(Code.ERROR, "有点抽风，工程师正在拼命!", EMPTY_OBJECT));
			return;
		}
		
		member.changeBalance( -money, "提现预扣款", BigInteger.ZERO, CreditLog.Platfrom.JIETIAO365);
		
		renderJson(getReturnJson(Code.OK, "申请已提交，我们尽快处理您的提现申请", EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "type",  must = true, type = ParamInterceptor.Type.ENUM_STRING, allow_list= {"reg","findPwd","validMobile","chgMobile"},chs = "类型")
	public void sendCaptcha(){
		String mobile = getPara("mobile");
		String code = String.format("%06d", (int) (Math.random() * 1000000));
		String type = getPara("type");
		String template = null;
		if(type.equals("reg")) template = "SMS_94665102";
		else if(type.equals("findPwd")) template = "SMS_94665102";
		else if(type.equals("validMobile"))  template = "SMS_94665102";
		else if(type.equals("chgMobile")){
			User user = UserQuery.me().findUserByMobile(mobile);
			if(user != null){
				renderJson(getReturnJson(Code.ERROR, "该手机已经注册帐号", EMPTY_OBJECT));
				return;
			}
			template = "SMS_94665102";
		}
		else {
			return;
		}
		
		Captcha existsCapche = CaptchaQuery.me().getCaptcha(mobile);
		if(existsCapche != null &&
				existsCapche.getCreateTime().getTime() + 60 * 1000 > System.currentTimeMillis()){
			renderJson(getReturnJson(Code.ERROR, "发送频繁，请在一分钟后重试", EMPTY_OBJECT));
			return;
		}
		
		JSONObject json = new JSONObject();
		json.put("code", code);
		try {
			AliSms.sendSms(mobile, template, json);
			Captcha captcha = getModel(Captcha.class);
			captcha.setCode(code);
			captcha.setType(type);
			captcha.setMobile(mobile);
			captcha.setIp(this.getIPAddress());
			captcha.setCreateTime(new Date());
			captcha.save();
			renderJson(getReturnJson(Code.OK, "发送成功", EMPTY_OBJECT));
		} catch (ClientException e) {
			renderJson(getReturnJson(Code.ERROR, "短信发送失败", EMPTY_OBJECT));
		}
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "captcha",  must = true, type = ParamInterceptor.Type.STRING, minlen=6, maxlen=6, chs = "验证码", minlenErrTips="验证码为6位", maxlenErrTips="验证码为6位")
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
		renderJson(getReturnJson(Code.OK, "验证码正确", EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "messageID",  must = true, type = ParamInterceptor.Type.INT, min=1,chs = "消息", minErrTips="消息不存在")
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
		renderJson(getReturnJson(Code.OK, "已阅读", EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	public void userClearMsg() {
		BigInteger memberID = getParaToBigInteger("memberID");
		int count = MessageQuery.me().deleteAll(memberID);
		JSONObject json = new JSONObject();
		json.put("count", ""+count);
		renderJson(getReturnJson(Code.OK, "已清空", json));
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
	@ParamAnnotation(name = "type",  must = true, type = ParamInterceptor.Type.STRING, minlen=1, chs = "消息类型", minlenErrTips="消息类型错误")
	public void clearBadge() {
		BigInteger memberID = getParaToBigInteger("memberID");
		int msg = 0;
		String type = getPara("type");
		if(type.equals("msg")) {
			msg = MessageQuery.me().readAll(memberID);
		}
		JSONObject json = new JSONObject();
		json.put("msg", ""+msg);
		renderJson(getReturnJson(Code.OK, "已清除", json));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = false, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "name",  must = false, type = ParamInterceptor.Type.STRING, minlen=1, chs = "联系人", minlenErrTips="请提供联系人")
	@ParamAnnotation(name = "tel",  must = false, type = ParamInterceptor.Type.STRING, minlen=1, chs = "联系电话", minlenErrTips="请提供联系电话")
	@ParamAnnotation(name = "content",  must = true, type = ParamInterceptor.Type.STRING, minlen=1, chs = "反馈内容", minlenErrTips="请提供反馈内容")
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
			renderJson(getReturnJson(Code.OK, "谢谢您向我们提交宝贵的反馈", json));
			return;
		} else {
			renderJson(getReturnJson(Code.ERROR, "新增反馈失败", EMPTY_OBJECT));
			return;
		}
		
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "content",  must = true, type = ParamInterceptor.Type.STRING, minlen=3, chs = "举报内容", minlenErrTips="请提供举报内容")
	@ParamAnnotation(name = "imgs",  must = false, type = ParamInterceptor.Type.STRING, chs = "举报图片")
	@ParamAnnotation(name = "toUserID",  must = false, type = ParamInterceptor.Type.INT, min=1, chs = "被举报人", minlenErrTips="请提供被举报人")
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
		report.setImgs(Utils.stripMultiMedia(imgs));
		report.setContent(content);
		report.setCreateTime(new Date());
		boolean flag = report.save();
		if(flag) {
			JSONObject json = new JSONObject();
			json.put("reportID", report.getId().toString());
			renderJson(getReturnJson(Code.OK, "举报已提交，请耐心等待我们的调查和回复", json));
			return;
		} else {
			renderJson(getReturnJson(Code.ERROR, "新增举报失败", EMPTY_OBJECT));
			return;
		}
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	public void applyAgreement(){
		BigInteger memberID = getParaToBigInteger("memberID");
		BigInteger applyID = getParaToBigInteger("applyID");
		Apply apply = ApplyQuery.me().findById(applyID);
		if(apply == null){
			String html = "<!DOCTYPE html><html lang=\"zh-CN\"><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><title>借款服务协议</title></head>\r\n" + 
					"<body>申请不存在</body></html>";
			this.renderHtml(html);
			return;
		}
		Apply.Status apply_status = Apply.Status.getEnum(apply.getStatus());
		if(apply_status == Apply.Status.INVALID){
			String html = "<!DOCTYPE html><html lang=\"zh-CN\"><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><title>借款服务协议</title></head>\r\n" + 
					"<body>申请已无效</body></html>";
			this.renderHtml(html);
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
				String html = "<!DOCTYPE html><html lang=\"zh-CN\"><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><title>借款服务协议</title></head>\r\n" + 
						"<body>内部错误</body></html>";
				this.renderHtml(html);
				return;
			}
			creditor = contract.getCreditUser();
			if(memberID.equals(creditor.getId()) == false && memberID.equals(debitor.getId()) == false) {
				String html = "<!DOCTYPE html><html lang=\"zh-CN\"><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><title>借款服务协议</title></head>\r\n" + 
						"<body>您不是当事方无权查看</body></html>";
				this.renderHtml(html);
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
			String html = "<!DOCTYPE html><html lang=\"zh-CN\"><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><title>借款服务协议</title></head>\r\n" + 
					"<body>协议生成失败</body></html>";
			this.renderHtml(html);
			return;
		}
		
		String webRoot = PathKit.getWebRootPath();
		agreement = agreement.substring(webRoot.length());
		String html = "<!DOCTYPE html><html lang=\"zh-CN\"><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=2, user-scalable=yes\"><title>借款服务协议</title></head>\r\n" + 
				"<body　style=\"padding:0px;margin:0px;\"><div　style=\"padding:0px;margin:0px;\"><img width=\"100%\" src=\""+Utils.toMedia(agreement)+"\"></div>"+
				      "<a style=\"position:fixed;bottom:2%;right:2%;padding:0px;margin:0px;\" href=\"/v1/downloadAgreement?source="+agreement+"&deleted=0\">下载</a>" + 
				"</body></html>";
		this.renderHtml(html);
		return;
	}
	
	@Clear(AccessTokenInterceptor.class)
	public void downloadAgreement(){
		String deleted = getPara("deleted", "0");
		String webRoot = PathKit.getWebRootPath();
		
		String source = getPara("source", "");
		if(StrKit.isBlank(source)){
			source = "/attachment/agreement/static/borrow-agreement.png";
			deleted = "0";
		} else {
			//attachment/agreement/201710/09/90a5640788e64d75bf9e121ac0c32091.png
			Pattern p = Pattern.compile("^\\/attachment\\/agreement\\/(permenant\\/)?20[12][0-9]{3}\\/[0-9]{2}\\/[0-9a-z]{10,}\\.png$");
			Matcher m = p.matcher(source);
			if(! m.find()){
				source = "/attachment/agreement/static/borrow-agreement.png";
				deleted = "0";
			}
			if(source.contains("permenant")) {
				deleted = "0";
			}
		}
		
		RenderFile render = new RenderFile();
		String mime = "application/pdf";
		render.setContext(this.getRequest(), this.getResponse(), webRoot + source, mime, "agreement.png", "1".equals(deleted));
		this.render(render);
	}
	
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "realname",  must = true, type = ParamInterceptor.Type.STRING, minlen=2, maxlen=4, chs = "真实姓名", minlenErrTips="真实姓名至少两个汉字", maxlenErrTips="真实姓名不超过四位")
	@ParamAnnotation(name = "idcard",  must = true, type = ParamInterceptor.Type.STRING, minlen=18, maxlen=18, chs = "身份证号", minlenErrTips="身份证号码为18位", maxlenErrTips="身份证号码为18位")
	@ParamAnnotation(name = "idcardimg1",  must = true, type = ParamInterceptor.Type.STRING, minlen=12, chs = "身份证正面照片", minlenErrTips="请上传身份证正面照片", mustErrTips="请上传身份证正面照片")
	@ParamAnnotation(name = "idcardimg2",  must = true, type = ParamInterceptor.Type.STRING, minlen=12, chs = "身份证反面照片", minlenErrTips="请上传身份证反面照片", mustErrTips="请上传身份证反面照片")
	public void verifyIDCard() {
		BigInteger memberID = getParaToBigInteger("memberID");
		String idcard = getPara("idcard").trim().toLowerCase();
		String realname = getPara("realname").trim().toLowerCase();
		String idcardimg1 = getPara("idcardimg1").trim();
		String idcardimg2 = getPara("idcardimg2").trim();
		
		User member = UserQuery.me().findByIdNoCache(memberID);
		int authCard = member.getAuthCard();
		if(authCard == 1 || authCard == 2) {
			renderJson(getReturnJson(Code.ERROR, authCard == 1 ? "已认证，请勿重复认证" : "认证中，请勿重复提交", EMPTY_OBJECT));
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
			renderJson(getReturnJson(Code.ERROR, "实名认证失败", EMPTY_OBJECT));
			return;
		}
		
		try {
			//获取生日
			SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyyMMdd");
			sdfYmd.setLenient(false);
			Date birthday = sdfYmd.parse(idcard.substring(6, 14));
			member.setBirthday(birthday);
			double canBorrow = Option.calCanBorrowMoney(birthday);
			member.setCanBorrowMoney((int) canBorrow);
		} catch (ParseException e) {
			//renderJson(getReturnJson(Code.ERROR, "实名认证失败", EMPTY_OBJECT));
			//return;
		}
		
		member.setAuthCard(1);
		member.setIdcard(idcard);
		member.setRealname(realname);
		
		member.setIdcardFront(Utils.stripMedia(idcardimg1));
		member.setIdcardBack(Utils.stripMedia(idcardimg2));
		member.update();
		
		renderJson(getReturnJson(Code.OK, "实名认证成功", EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	@ParamAnnotation(name = "idcard",  must = false, type = ParamInterceptor.Type.STRING, minlen=18, maxlen=18, chs = "身份证号", minlenErrTips="身份证号码为18位", maxlenErrTips="身份证号码为18位")
	@ParamAnnotation(name = "realname",  must = false, type = ParamInterceptor.Type.STRING, minlen=2, maxlen=4, chs = "真实姓名", minlenErrTips="姓名至少两个汉字", maxlenErrTips="姓名不超过四位")
	@ParamAnnotation(name = "typeid",  must = false, type = ParamInterceptor.Type.INT, min=1, max=7, chs = "证件类型", minErrTips="证件类型错误", maxErrTips="证件类型错误")
	@ParamAnnotation(name = "payType",  must = true, type = ParamInterceptor.Type.ENUM_STRING, chs = "支付方式", allow_list={"unionPay","balancePay"}, allowListErrTips="不支持该支付方式")
	public void verifyMobile(){
		BigInteger memberID = getParaToBigInteger("memberID");
		String mobile = getPara("mobile");
		
		@SuppressWarnings("unused")
		String idcard = getPara("idcard", "");
		@SuppressWarnings("unused")
		String realname = getPara("realname", "");
		@SuppressWarnings("unused")
		Integer typeid = getParaToInt("typeid", 1);
		
		String payType = getPara("payType");
		
		JSONObject json = new JSONObject();
		json.put("payType", payType);
		json.put("orderSN", "");
		json.put("tn", "");
		
		User member = UserQuery.me().findByIdNoCache(memberID);
		if(!mobile.equals(member.getMobile())){
			renderJson(getReturnJson(Code.ERROR, "该手机号与您注册的手机号不同，如需修改请联系客服", json));
			return;
		}
		
		// 0:未验证; 1: 已验证； 2: 验证中
		String mobileStatus = member.getMobileStatus();
		if("2".equals(mobileStatus)){
			renderJson(getReturnJson(Code.ERROR, "请勿重复申请手机号认证", json));
			return;
		}
		
		if("1".equals(mobileStatus)){
			String mobileStatusLogic = member.getMobileStatusLogic();
			if("1".equals(mobileStatusLogic)) {
				renderJson(getReturnJson(Code.ERROR, "已认证，请勿重复认证", json));
				return;
			} else {
				//续期认证
				if("balancePay".equals(payType)) {
					if(member.getAmount().doubleValue() >= 10.0) {
						boolean flag = member.changeBalance(-10.0, "手机认证服务费", BigInteger.ZERO, CreditLog.Platfrom.JIETIAO365);
						if(flag == false) {
							log.error("用户 " + memberID.intValue() + " 手机认证服务费扣款失败");
							renderJson(getReturnJson(Code.ERROR, "扣款失败，请稍后重试", json));
							return;
						} else {
							member.setAuthExpire(Utils.getNextMonthDay());
							member.update();
							renderJson(getReturnJson(Code.OK, "手机号认证成功", json));
							return;
						}
					} else {
						renderJson(getReturnJson(Code.ERROR, "余额不足，手机号认证服务费10元", json));
						return;
					}
				} else {
					//银联支付
					UnionAppPayMethod.Result result = UnionAppPayMethod.chargePay(memberID, 10.0, UnionAppPayMethod.PayType.MOBILE_VERIFY);
					UnionpayLog unionpayLog = result.getUnionpayLog();
					String err = result.getErr();
					if(unionpayLog != null) {
						//返回给前端，前端凭tn码完成具体的支付操作
						json.put("payType", payType);
						json.put("orderSN", unionpayLog.getPaySn());
						json.put("tn", unionpayLog.getTn());
						renderJson(getReturnJson(Code.OK, "", json));
						return;
					} else {
						renderJson(getReturnJson(Code.ERROR, err != null ? err : "服务暂时不可用，请稍后重试", json));
						return;
					}
				}
			}
		}
		
		int authCard = member.getAuthCard();
		if(authCard == 0 || authCard == 2){
			renderJson(getReturnJson(Code.ERROR, authCard == 0 ? "请先进行实名认证" : "实名认证中，请稍后提交手机号认证", json));
			return;
		}
		realname = member.getRealname();
		idcard = member.getIdcard();
		
		//开始验证
		boolean flag = MobileVerify.verify(idcard,mobile,  realname, typeid.toString());
		if(flag == false) {
			renderJson(getReturnJson(Code.ERROR, "手机号与实名认证信息不匹配", json));
			return;
		}
		member.setMobileStatus("1");
		member.update();
		
		if("balancePay".equals(payType)) {
			if(member.getAmount().doubleValue() >= 10.0) {
				flag = member.changeBalance(-10.0, "手机认证服务费", BigInteger.ZERO, CreditLog.Platfrom.JIETIAO365);
				if(flag == false) {
					log.error("用户 " + memberID.intValue() + " 手机认证服务费扣款失败");
					renderJson(getReturnJson(Code.ERROR, "扣款失败，请稍后重试", json));
					return;
				} else {
					member.setAuthExpire(Utils.getNextMonthDay());
					member.update();
					renderJson(getReturnJson(Code.OK, "手机号认证成功", json));
					return;
				}
			} else {
				renderJson(getReturnJson(Code.ERROR, "余额不足，手机号认证服务费10元", json));
				return;
			}
		} else {
			//银联支付
			UnionAppPayMethod.Result result = UnionAppPayMethod.chargePay(memberID, 10.0, UnionAppPayMethod.PayType.MOBILE_VERIFY);
			UnionpayLog unionpayLog = result.getUnionpayLog();
			String err = result.getErr();
			if(unionpayLog != null) {
				//返回给前端，前端凭tn码完成具体的支付操作
				json.put("payType", payType);
				json.put("orderSN", unionpayLog.getPaySn());
				json.put("tn", unionpayLog.getTn());
				renderJson(getReturnJson(Code.OK, "", json));
				return;
			} else {
				renderJson(getReturnJson(Code.ERROR, err != null ? err : "服务暂时不可用，请稍后重试", json));
				return;
			}
		}
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "realname",  must = false, type = ParamInterceptor.Type.STRING, minlen=2, maxlen=4, chs = "真实姓名", minlenErrTips="姓名至少两个汉字", maxlenErrTips="姓名不超过四位")
	@ParamAnnotation(name = "idcard",  must = false, type = ParamInterceptor.Type.STRING, minlen=18, maxlen=18, chs = "身份证号", minlenErrTips="身份证号码为18位", maxlenErrTips="身份证号码为18位")
	@ParamAnnotation(name = "bankcard",  must = true, type = ParamInterceptor.Type.STRING, minlen=12, maxlen=30, chs = "银行卡号", minlenErrTips="银行卡号格式不正确", maxlenErrTips="银行卡号格式不正确")
	@ParamAnnotation(name = "mobile",  must = false, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	public void verifyBankCard() {
		BigInteger memberID = getParaToBigInteger("memberID");
		String bankcard = getPara("bankcard");
		String mobile = getPara("mobile", "");
		
		@SuppressWarnings("unused")
		String idcard = getPara("idcard", "");
		@SuppressWarnings("unused")
		String realname = getPara("realname", "");
		
		User member = UserQuery.me().findByIdNoCache(memberID);
		
		int authBank = member.getAuthBank();
		if(authBank == 1 || authBank == 2) {
			renderJson(getReturnJson(Code.ERROR, authBank == 1 ? "已认证，请勿重复认证" : "请勿重复提交认证申请", EMPTY_OBJECT));
			return;
		}
		
		int authCard = member.getAuthCard();
		if(authCard == 0 || authCard == 2) {
			renderJson(getReturnJson(Code.ERROR, authCard == 0 ? "请先进行实名认证" : "实名认证中，请稍后提交银行卡认证", EMPTY_OBJECT));
			return;
		}else {
			idcard = member.getIdcard();
			realname = member.getRealname();
		}
		
		/*
		String mobileStatus = member.getMobileStatusLogic();
		if("0".equals(mobileStatus)){
			renderJson(getReturnJson(Code.ERROR, "请先认证手机号码", EMPTY_OBJECT));
			return;
		}
		if("2".equals(mobileStatus)){
			renderJson(getReturnJson(Code.ERROR, "手机号认证中，请稍后提起银行卡认证", EMPTY_OBJECT));
			return;
		}
		*/
		if(StrKit.notBlank(mobile) ) {
			if(!mobile.equals(member.getMobile())) {
				renderJson(getReturnJson(Code.ERROR, "该手机号与您注册的手机号不同，如需修改请联系客服", EMPTY_OBJECT));
				return;
			}
		} else {
			mobile = member.getMobile();
		}
		
		//开始验证
		boolean flag = BankcardVerify.verify(bankcard, idcard,mobile, realname);
		//boolean flag = true;
		if(flag == false) {
			renderJson(getReturnJson(Code.ERROR, "银行卡认证失败", EMPTY_OBJECT));
			return;
		}
		
		BankcardDetail bankcardDetail = BankcardDetail.verify(bankcard);
		if(bankcardDetail != null){
			member.setBanktype(bankcardDetail.getHtml());
		}
		//更新银行卡的认证情况
		member.setBankcard(bankcard);
		member.setAuthBank(1);
		member.update();
		
		renderJson(getReturnJson(Code.OK, "银行卡认证成功", EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "faceimg",  must = true, type = ParamInterceptor.Type.STRING, minlen=12, chs = "人脸近照", minlenErrTips="人脸近照错误")
	public void verifyFace() {
		BigInteger memberID = getParaToBigInteger("memberID");
		String faceimg = getPara("faceimg").trim();
		User member = UserQuery.me().findByIdNoCache(memberID);
		
		int authFace = member.getAuthFace();
		if( authFace == 1 || authFace == 2) {
			renderJson(getReturnJson(Code.ERROR, authFace == 1 ? "人脸已认证，请勿重复认证" : "人脸认证已提交，请勿重复提交", EMPTY_OBJECT));
			return;
		}
		
		int authCard = member.getAuthCard();
		if(authCard == 0 || authCard == 2) {
			renderJson(getReturnJson(Code.ERROR, authCard == 0 ? "请先完成实名认证" : "实名认证中，请稍后提起人脸认证", EMPTY_OBJECT));
			return;
		}
		
		member.setFaceimg(Utils.stripMedia(faceimg));
		member.setAuthFace(1);
		member.update();
		//后台确认比对后，将auth_face设为1
		renderJson(getReturnJson(Code.OK, "肖像认证已经通过", EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "books",  must = true, type = ParamInterceptor.Type.STRING, minlen=1, chs = "通讯录", minlenErrTips="通讯录不为空")
	public void verifyBook() {
		BigInteger memberID = getParaToBigInteger("memberID");
		String books = "";
		try {
			String booksUrlencode = getPara("books").trim();
			String cc = URLDecoder.decode(booksUrlencode, "UTF-8").replace(" ", "+");
			String _books = new String(Base64.decodeBase64(cc), "UTF-8");
			if(_books != null) books = _books;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		User member = UserQuery.me().findByIdNoCache(memberID);
		member.setBooks(books);
		member.setAuthBook(1);
		member.update();
		renderJson(getReturnJson(Code.OK, "通讯录认证已经通过", EMPTY_OBJECT));
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "fee",  must = true, type = ParamInterceptor.Type.DOUBLE, mind=0.01, chs = "充值金额", minErrTips="充值金额不少于0.01")
	public void createOrder() {
		BigInteger memberID = getParaToBigInteger("memberID");
		String feeStr = getPara("fee");
		double fee =  Double.parseDouble(feeStr);
		
		UnionAppPayMethod.Result result = UnionAppPayMethod.chargePay(memberID, fee, UnionAppPayMethod.PayType.CHARGE);
		UnionpayLog unionpayLog = result.getUnionpayLog();
		String err = result.getErr();
		if(unionpayLog != null) {
			//返回给前端，前端凭tn码完成具体的支付操作
			JSONObject json = new JSONObject();
			json.put("orderSN", unionpayLog.getPaySn());
			json.put("tn", unionpayLog.getTn());
			renderJson(getReturnJson(Code.OK, "", json));
			return;
		} else {
			renderJson(getReturnJson(Code.ERROR, err != null ? err : "服务暂时不可用，请稍后重试", EMPTY_OBJECT));
			return;
		}
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "orderSN",  must = true, type = ParamInterceptor.Type.STRING, minlen=10, chs = "充值订单号", minlenErrTips="充值订单号错误")
	public void queryOrder() {
		BigInteger memberID = getParaToBigInteger("memberID");
		String paySn = getPara("orderSN");
		UnionpayLog unionpayLog = UnionpayLogQuery.me().findByPaySn(paySn);
		if(unionpayLog == null) {
			renderJson(getReturnJson(Code.ERROR, "充值订单号不存在", EMPTY_OBJECT));
			return;
		}
		if(! memberID.equals(unionpayLog.getUserId())) {
			renderJson(getReturnJson(Code.ERROR, "无权查询", EMPTY_OBJECT));
			return;
		}
		if(unionpayLog.getStatus() == 1) {
			renderJson(getReturnJson(Code.OK, "支付成功", EMPTY_OBJECT));
			return;
		} else {
			//主动查询交易是否成功
			UnionAppPay unionAppPay = new UnionAppPay();
			boolean flag = unionAppPay.query(paySn, Utils.getDayNumber(unionpayLog.getCreateTime()));
			if(flag == true) {
				// 考虑到，如果后端通知接口和前端查询接口存在竞争，导致线程不安全，此处仅告知成功但不更新数据
				renderJson(getReturnJson(Code.OK, "支付已经成功，稍后将转入您的余额账户中", EMPTY_OBJECT));
				return;
			} else {
				renderJson(getReturnJson(Code.ERROR, "交易处理中，请耐心等待。如充值30分钟后仍未到帐，请联系客服处理。", EMPTY_OBJECT));
				return;
			}
		}
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "memberToken",  must = true, type = ParamInterceptor.Type.MEMBER_TOKEN, chs = "用户令牌")
	@ParamAnnotation(name = "deviceToken",  must = true, type = ParamInterceptor.Type.STRING, minlen=1, chs = "DeviceToken", minlenErrTips="DeviceToken错误")
	public void uploadDeviceToken(){
		BigInteger memberID = getParaToBigInteger("memberID");
		String deviceToken = getPara("deviceToken");
		User member = UserQuery.me().findByIdNoCache(memberID);
		member.setDeviceToken(deviceToken);
		member.update();
		renderJson(getReturnJson(Code.OK, "设置成功", EMPTY_OBJECT));
		return;
	}
	
	//@SuppressWarnings("unused")
	@Clear(AccessTokenInterceptor.class)
	public void getAccessToken(){
		
		//String salt = EncryptUtils.salt();
		//String pwd = EncryptUtils.encryptPassword("123456", salt);
		
		//renderJson(getReturnJson(Code.OK, salt + "  " + pwd, EMPTY_OBJECT));
		Date d = new Date();
		JSONObject c = new JSONObject();
		c.put("money", "10.00");
		c.put("date1", Utils.toYmd(d));
		c.put("date2", Utils.toYmd(Utils.getPrevDay(d)) + " 22:00");
		new YuxinSms("18968596872", c.toJSONString(), "414215").send();
		renderJson(getReturnJson(Code.OK, "OK", EMPTY_OBJECT));
		return;
		/*
		//String sign = ChinapayUtils.test();
		//renderHtml(sign);
		User user = UserQuery.me().findByIdNoCache(BigInteger.valueOf(8L));
		BalanceAddPush bap = new BalanceAddPush(user.getDeviceToken(), "林忠仁", 100.0);
		Push.send(bap);
		ChargeFailPush cfp = new ChargeFailPush(user.getDeviceToken(), new Date(), 200.00);
		Push.send(cfp);
		LendoutPush lop = new LendoutPush(user.getDeviceToken(), "林忠仁", 300.0);
		Push.send(lop);
		
		renderJson(getReturnJson(Code.OK, "OK", EMPTY_OBJECT));
		return;
		*/
		/*if(DEBUG == false) return;
		renderJson(getReturnJson(Code.OK, AccessTokenInterceptor.getCurrentAccessToken(), EMPTY_OBJECT));
		return;
		*/
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
							"<form action='/v1/uploadFile?memberToken="+member.getMemberToken()+"&accessToken=" + AccessTokenInterceptor.getCurrentAccessToken() + "' method='post' enctype='multipart/form-data'>"+
				                 "<input type='file' name='file'>" +
				                 "<input type='hidden' name='memberID' value='1'>" + 
							     "<input type='submit' value='submit'>"+
				            "</form>"
				          +"</body></html>");
	}
}
