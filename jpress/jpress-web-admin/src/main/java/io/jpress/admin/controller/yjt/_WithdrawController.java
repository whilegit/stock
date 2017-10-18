package io.jpress.admin.controller.yjt;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.core.perm.PermAnnotation;
import yjt.model.Withdraw;
import yjt.model.query.WithdrawQuery;

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
	
	@PermAnnotation("contract-stat")
	public void stat(){
		this.renderText("功能暂时未完成");
	}
}
