package io.jpress.admin.controller.yjt;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.core.perm.PermAnnotation;
import yjt.model.Ad;
import yjt.model.query.AdQuery;


@RouterMapping(url = "/admin/ad", viewPath = "/WEB-INF/admin/ad")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _AdController extends JBaseCRUDController<Ad>{
	private String getModuleName() {
		return getPara("m");
	}

	private String getStatus() {
		return getPara("s");
	}
	
	@PermAnnotation("ad-list")
	public void index(){
		
		setAttr("StatusValidCode", Ad.Status.VALID.getIndex());	
		setAttr("StatusInvalidCode", Ad.Status.INVALID.getIndex());		
		
		int count = (int) AdQuery.me().findCount(null);
		setAttr("count", count);
		int valid_count = (int) AdQuery.me().findCount(Ad.Status.VALID);
		setAttr("valid_count", valid_count);
		int invalid_count =  (int) AdQuery.me().findCount(Ad.Status.INVALID);
		setAttr("invalid_count", invalid_count);
		
		
		Page<Ad> page =  AdQuery.me().paginateBySearch(getPageNumber(), getPageSize(), null, getStatus());
		setAttr("page", page);
		
		setAttr("include", "_index_include.html");
		render("index.html");
	}
}
