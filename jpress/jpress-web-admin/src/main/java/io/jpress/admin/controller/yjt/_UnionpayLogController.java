package io.jpress.admin.controller.yjt;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.core.perm.PermAnnotation;
import yjt.model.UnionpayLog;
import yjt.model.query.FeedbackQuery;
import yjt.model.query.UnionpayLogQuery;


@RouterMapping(url = "/admin/unionpaylog", viewPath = "/WEB-INF/admin/unionpaylog")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _UnionpayLogController extends JBaseCRUDController<UnionpayLog>{
	
	@PermAnnotation("unionpaylog-list")
	public void index(){
		int count = (int) UnionpayLogQuery.me().findCount();
		setAttr("count", count);
		
		String keyword = getPara("k", "").trim();
		Page<UnionpayLog> page =  UnionpayLogQuery.me().paginateBySearch(getPageNumber(), getPageSize(), keyword, null);
		setAttr("page", page);
		
		setAttr("include", "_index_include.html");
		render("index.html");
	}
}
