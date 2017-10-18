package io.jpress.admin.controller.yjt;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.core.perm.PermAnnotation;
import yjt.model.Ad;
import yjt.model.Report;
import yjt.model.query.AdQuery;
import yjt.model.query.ReportQuery;


@RouterMapping(url = "/admin/report", viewPath = "/WEB-INF/admin/report")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _ReportController extends JBaseCRUDController<Report>{
	private String getModuleName() {
		return getPara("m");
	}

	private String getStatus() {
		return getPara("s");
	}
	
	@PermAnnotation("report-list")
	public void index(){
		
		setAttr("StatusBeforeCode", Report.Status.BEFORE.getIndex());	
		setAttr("StatusAfterCode", Report.Status.AFTER.getIndex());		
		
		int count = (int) ReportQuery.me().findCount(null);
		setAttr("count", count);
		int before_count = (int) ReportQuery.me().findCount(Report.Status.BEFORE);
		setAttr("before_count", before_count);
		int after_count =  (int) ReportQuery.me().findCount(Report.Status.AFTER);
		setAttr("after_count", after_count);
		
		String keyword = getPara("k", "").trim();
		Page<Report> page =  ReportQuery.me().paginateBySearch(getPageNumber(), getPageSize(), keyword, getStatus());
		setAttr("page", page);
		
		setAttr("include", "_index_include.html");
		render("index.html");
	}
}
