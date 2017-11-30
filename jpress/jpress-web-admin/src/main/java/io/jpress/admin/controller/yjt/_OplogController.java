package io.jpress.admin.controller.yjt;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.core.perm.PermAnnotation;
import yjt.model.Feedback;
import yjt.model.Oplog;
import yjt.model.query.OplogQuery;


@RouterMapping(url = "/admin/oplog", viewPath = "/WEB-INF/admin/oplog")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _OplogController extends JBaseCRUDController<Feedback>{
	
	@PermAnnotation("oplog-list")
	public void index(){	
		int count = (int) OplogQuery.me().findCount();
		setAttr("count", count);
		
		String keyword = getPara("k", "").trim();
		Page<Oplog> page =  OplogQuery.me().paginateBySearch(getPageNumber(), getPageSize(), keyword);
		setAttr("page", page);
		
		setAttr("include", "_index_include.html");
		render("index.html");
	}
}
