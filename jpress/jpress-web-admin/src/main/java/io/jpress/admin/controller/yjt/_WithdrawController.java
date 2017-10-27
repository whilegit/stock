package io.jpress.admin.controller.yjt;

import java.math.BigInteger;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
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
		
		
		int count = (int) WithdrawQuery.me().findCount(null);
		setAttr("count", count);
		int trans_count = (int) WithdrawQuery.me().findCount(Withdraw.Status.TRANS);
		setAttr("trans_count", trans_count);
		int untrans_count =  (int) WithdrawQuery.me().findCount(Withdraw.Status.UNTRANS);
		setAttr("untrans_count", untrans_count);
		
		
		String keyword = getPara("k", "").trim();
		Page<Withdraw> page =  WithdrawQuery.me().paginateBySearch(getPageNumber(), getPageSize(), keyword, getStatus());
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
		
		Withdraw withdraw = WithdrawQuery.me().findById(id);
		if(withdraw == null) {
			renderText("错误：提现不存在");
			return;
		}
		
		if(!StrKit.notBlank(bankCard)) {
			renderText("错误：卡号不能为空");
			return;
		}
		
		if(!StrKit.notBlank(province, city)) {
			renderText("错误：开户省份或开户城市不能为空");
			return;
		}
		
		if(!StrKit.notBlank(comment)) {
			renderText("错误：操作说明不能为空");
			return;
		}

				
		float money = 0.00f;
		try {
			money = Float.parseFloat(moneyStr);
			money *= 100;
			if(money < 0.00) throw new Exception();
		} catch (Exception e) {
			renderText("错误：提现金额不正确");
			return;
		}
		
		User user = UserQuery.me().findByIdNoCache(withdraw.getUserId());
		if(user == null) {
			renderText("错误：提现用户不存在");
			return;
		}
		String cardNo = user.getBankcard();
		String userName = user.getRealname();

		ChinapayTransBean trans = ChinapayUtils.getTransBean(cardNo, userName, openbank, province, city, (int)money, "余额提现");
		PayStatus result = ChinapayUtils.pay(trans);
		if(result == null) {
			renderText("错误：交易");
			return;
		}
		
		
		int status = result.success ? Withdraw.Status.TRANS.getIndex() : Withdraw.Status.FAIL.getIndex();
		String logStr = withdraw.getLog();
		withdraw.setLog(logStr + "\r\n" + comment);
		withdraw.setCode(result.raw);
		withdraw.setStatus(status);
		if(result.success) {
			withdraw.setPayedTime(new Date());
		}
		withdraw.setUpdateTime(new Date());
		
		withdraw.setClerk(getLoginedUser().getId());
		withdraw.update();
		renderText(result.tips);
	}
	
	@PermAnnotation("contract-stat")
	public void stat(){
		this.renderText("功能暂时未完成");
	}
}
