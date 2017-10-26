package io.jpress.admin.controller.yjt;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.core.perm.PermAnnotation;
import yjt.model.Apply;
import yjt.model.query.ApplyQuery;

@RouterMapping(url = "/admin/apply", viewPath = "/WEB-INF/admin/apply")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _ApplyController extends JBaseCRUDController<Apply>{
	private String getModuleName() {
		return getPara("m");
	}

	private String getStatus() {
		return getPara("s");
	}
	
	@PermAnnotation("apply-list")
	public void index(){
		setAttr("StatusInvalidCode", ApplyQuery.Status.INVALID.getIndex());	
		setAttr("StatusValidCode", ApplyQuery.Status.VALID.getIndex());
		setAttr("StatusDealedCode", ApplyQuery.Status.DEALED.getIndex());
		setAttr("StatusTimeoutCode", ApplyQuery.Status.TIMEOUT.getIndex());
		
		int count = (int) ApplyQuery.me().findCount(null);
		setAttr("count", count);
		int dealed_count = (int) ApplyQuery.me().findCount(ApplyQuery.Status.DEALED);
		setAttr("dealed_count", dealed_count);
		int valid_count =  (int) ApplyQuery.me().findCount(ApplyQuery.Status.VALID);
		setAttr("valid_count", valid_count);
		int timeout_count =  (int) ApplyQuery.me().findCount(ApplyQuery.Status.TIMEOUT);
		setAttr("timeout_count", timeout_count);
		int invalid_count =  (int) ApplyQuery.me().findCount(ApplyQuery.Status.INVALID);
		setAttr("invalid_count", invalid_count);
		
		String keyword = getPara("k", "").trim();
		Page<Apply> page = null;
		page = ApplyQuery.me().paginateBySearch(getPageNumber(), getPageSize(), keyword, getStatus());
		setAttr("page", page);
		
		setAttr("include", "_index_include.html");
		render("index.html");
	}
	
	/*
	@PermAnnotation("contract-edit")
	public void edit(){
		int cid =  (getPara("cid") != null) ? Integer.parseInt(getPara("cid")) : 0;
		Contract contract = ContractQuery.me().findById(BigInteger.valueOf(cid));
		if(contract == null) {
			this.renderError(-1);
			return;
		}
		setAttr("contract", contract);
		setAttr("include", "_edit_include.html");
		render("edit.html");
	}
	*/
	
	@PermAnnotation("contract-stat")
	public void stat(){
		this.renderText("功能暂时未完成");
	}
}
