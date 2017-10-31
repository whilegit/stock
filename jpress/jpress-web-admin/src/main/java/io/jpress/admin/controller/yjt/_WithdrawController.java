package io.jpress.admin.controller.yjt;

import java.math.BigInteger;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.Utils;
import yjt.ChinaPay.ChinapayTransBean;
import yjt.ChinaPay.ChinapayUtils;
import yjt.ChinaPay.ChinapayUtils.PayStatus;
import yjt.core.perm.PermAnnotation;
import yjt.model.Withdraw;
import yjt.model.query.WithdrawQuery;
import yjt.verify.BankcardDetail;

@RouterMapping(url = "/admin/withdraw", viewPath = "/WEB-INF/admin/withdraw")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _WithdrawController extends JBaseCRUDController<Withdraw>{
	private String getModuleName() {
		return getPara("m");
	}

	private String getStatus() {
		return getPara("s");
	}
	
	@PermAnnotation("withdraw-list")
	public void index(){
		setAttr("StatusTransCode", Withdraw.Status.TRANS.getIndex());	
		setAttr("StatusUnTransCode", Withdraw.Status.UNTRANS.getIndex());
		
		int count = 0;
		Integer today = getParaToInt("today", 0);
		if(today == 0) {
			count = (int) WithdrawQuery.me().findCount(null);
		} else {
			count = (int) WithdrawQuery.me().findCountToday();
		}
		setAttr("today", today);
		setAttr("count", count);
		int trans_count = (int) WithdrawQuery.me().findCount(Withdraw.Status.TRANS);
		setAttr("trans_count", trans_count);
		int untrans_count =  (int) WithdrawQuery.me().findCount(Withdraw.Status.UNTRANS);
		setAttr("untrans_count", untrans_count);
		
		
		String keyword = getPara("k", "").trim();
		Page<Withdraw> page =  WithdrawQuery.me().paginateBySearch(getPageNumber(), getPageSize(), keyword, getStatus(),  today==1?Utils.getTodayStartTime():null);
		setAttr("page", page);
		
		setAttr("include", "_index_include.html");
		render("index.html");
	}
	
	@PermAnnotation("withdraw-pay")
	public void prepay() {
		BigInteger id = getParaToBigInteger("id", BigInteger.ZERO);
		Withdraw draw = WithdrawQuery.me().findById(id);
		setAttr("draw", draw);
		if(draw != null) {
			User u = UserQuery.me().findById(draw.getUserId());
			if(u != null) {
				String banktype = u.getBanktype();
				if(StrKit.notBlank(banktype)) {
					BankcardDetail bankdetail = JSON.parseObject(banktype, BankcardDetail.class);
					 if(bankdetail != null) {
						 setAttr("bankdetail", bankdetail);
						 setAttr("id", id);
					 }
				}
				setAttr("user", u);
			}
		}
		render("prepay.html");
	}
	
	@PermAnnotation("withdraw-pay")
	public void dopay() {
		String province = this.getPara("draw[province]", "");
		String city = this.getPara("draw[city]", "");
		String comment = this.getPara("draw[comment]", "");
		String moneyStr = this.getPara("draw[money]", "");
		String bankCard = this.getPara("draw[bank_account]", "");
		String openbank = this.getPara("draw[openbank]", "");
		BigInteger id = this.getParaToBigInteger("id", BigInteger.ZERO);
		
		setAttr("p", "draw");
		setAttr("href", this.getRequest().getHeader("referer"));
		setAttr("include", "_dopay_include.html");
		
		String errMsg = null;
		Withdraw withdraw = WithdrawQuery.me().findById(id);
		if(withdraw == null) {
			errMsg = "错误：提现不存在";
			setAttr("errMsg", errMsg);
			render("dopay.html");
			return;
		}
		if(withdraw.getStatus() != Withdraw.Status.UNTRANS.getIndex()) {
			errMsg = "错误：只能处理未打款的提现申请，如打款失败请人工核对再手动打款。";
			setAttr("errMsg", errMsg);
			render("dopay.html");
			return;
		}
		
		if(!StrKit.notBlank(bankCard)) {
			errMsg = "错误：卡号不能为空";
			setAttr("errMsg", errMsg);
			render("dopay.html");
			return;
		}
		
		if(!StrKit.notBlank(province, city)) {
			errMsg = "错误：开户省份或开户城市不能为空";
			setAttr("errMsg", errMsg);
			render("dopay.html");
			return;
		}
		
		if(!StrKit.notBlank(comment)) {
			errMsg = "错误：操作说明不能为空";
			setAttr("errMsg", errMsg);
			render("dopay.html");
			return;
		}

		float money = 0.00f;
		try {
			money = Float.parseFloat(moneyStr);
			money *= 100;
			if(money < 0.00) throw new Exception();
		} catch (Exception e) {
			errMsg = "错误：提现金额不正确";
			setAttr("errMsg", errMsg);
			render("dopay.html");
			return;
		}

		User user = UserQuery.me().findByIdNoCache(withdraw.getUserId());
		if(user == null) {
			errMsg = "错误：提现用户不存在";
			setAttr("errMsg", errMsg);
			render("dopay.html");
			return;
		}
		
		String cardNo = user.getBankcard();
		String userName = user.getRealname();

		ChinapayTransBean trans = ChinapayUtils.getTransBean(cardNo, userName, openbank, province, city, (int)money, "余额提现");
		if(trans == null) {
			errMsg ="错误：参数错误";
			setAttr("errMsg", errMsg);
			render("dopay.html");
			return;
		}
		
		PayStatus paystatus = ChinapayUtils.pay(trans);
		if(paystatus == null) {
			renderText("错误：未知错误");
			return;
		}
		
		int status = paystatus.success ? Withdraw.Status.TRANS.getIndex() : Withdraw.Status.FAIL.getIndex();
		String logStr = withdraw.getLog();
		withdraw.setLog(logStr + "\r\n" + comment);
		withdraw.setCode(paystatus.raw);
		withdraw.setStatus(status);
		if(paystatus.success) {
			withdraw.setPayedTime(new Date());
			String banktype = user.getBanktype();
			if(StrKit.notBlank(banktype)) {
				BankcardDetail bankdetail = JSON.parseObject(banktype, BankcardDetail.class);
				 if(bankdetail != null) {
					 BankcardDetail.Result br = bankdetail.getResult();
					 if(br != null) {
						 boolean updateFlag = false;
						 if(! province.equals(br.getProvince())) {
							 br.setProvince(province);
							 updateFlag = true;
						 }
						 if(!city.equals(br.getCity())) {
							 br.setCity(city);
							 updateFlag = true;
						 }
						 if(updateFlag) {
							 user.setBanktype(JSONObject.toJSONString(bankdetail));
							 user.update();
						 }
					 }
				 }
			}
		}
		withdraw.setUpdateTime(new Date());
		withdraw.setClerk(getLoginedUser().getId());
		withdraw.update();
		
		setAttr("paystatus", paystatus);
		render("dopay.html");
	}
	
	@PermAnnotation("contract-stat")
	public void stat(){
		this.renderText("功能暂时未完成");
	}
}
