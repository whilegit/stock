package yjt.api.v1;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
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
import yjt.model.Apply;
import yjt.model.Captcha;
import yjt.model.Contract;
import yjt.model.Feedback;
import yjt.model.Follow;
import yjt.model.Message;
import yjt.model.Report;
import yjt.model.query.ApplyQuery;
import yjt.model.query.CaptchaQuery;
import yjt.model.query.FollowQuery;
import yjt.model.query.MessageQuery;
import yjt.api.v1.Interceptor.*;
import yjt.Utils;
import yjt.api.v1.Annotation.*;


@RouterMapping(url="/v1")
@Before(AccessTokenInterceptor.class)
@Clear(JI18nInterceptor.class)
public class IndexController extends ApiBaseController {
	
	private static final boolean DEBUG = true;
	protected static final SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy-MM-dd");
	
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
	public void index(){
		renderJson(getReturnJson(Code.OK, "Hello Index", EMPTY_OBJECT));
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
	@ParamAnnotation(name = "repaymentMethod",  must = false, type = ParamInterceptor.Type.INT, min=1, max=3,chs = "还款方式")
	public void payApply() {
		BigInteger memberID = getParaToBigInteger("memberID");
		User member = UserQuery.me().findByIdNoCache(memberID);
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
		
		int repaymentMethod = getParaToInt("repaymentMethod", Contract.getDefaultReportMethod().getIndex());
		JSONObject result = Contract.createContract(apply, member, debitor, Contract.RepaymentMethod.getEnum(repaymentMethod));
		if(StrKit.notBlank(result.getString("err"))) {
			renderJson(getReturnJson(Code.ERROR, result.getString("err"), EMPTY_OBJECT));
			return;
		}
		renderJson(getReturnJson(Code.OK, "", ((Contract)result.get("contract")).getProfile())); 
		return;
	}
	
	@Before(ParamInterceptor.class)
	@ParamAnnotation(name = "mobile",  must = true, type = ParamInterceptor.Type.MOBILE, chs = "手机号")
	public void sendCaptcha(){
		String mobile = getPara("mobile");
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
