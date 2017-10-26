package io.jpress.admin.controller.yjt;

import java.math.BigInteger;

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
		/*
		//{
		  	"error_code":0,
			"reason":"Succes",
			"result":{
				"bankname":"浙江省农村信用社联合社",
				"banknum":"14293300",
				"cardprefixnum":"622858",
				"cardname":"丰收卡(银联卡)",
				"cardtype":"银联借记卡",
				"cardprefixlength":"6",
				"cardlength":"19",
				"isLuhn":true,
				"iscreditcard":1,
				"bankurl":"",
				"enbankname":"",
				"abbreviation":"ZJNX",
				"bankimage":"http:\/\/auth.apis.la\/bank\/114_ZJNX.png",
				"servicephone":""
			},
			"ordersign":"20171016122721798557515653"
		}
		*/
		render("prepay.html");
	}
	
	@PermAnnotation("withdraw-pay")
	public void dopay() {
		renderText("打款已成功");
	}
	
	@PermAnnotation("contract-stat")
	public void stat(){
		this.renderText("功能暂时未完成");
	}
}
