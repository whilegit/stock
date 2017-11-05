package io.jpress.admin.controller.yjt;

import java.math.BigInteger;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.model.User;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.core.perm.PermAnnotation;
import yjt.model.Report;
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
	
	@PermAnnotation("report-delete")
	public void delete(){
		BigInteger id = getParaToBigInteger("id");
		if (id == null) {
			renderAjaxResultForError();
			return;
		}
		Report ad = ReportQuery.me().findById(id);
		ad.delete();
	}
	
	@PermAnnotation("report-proc")
	public void proc() {
		User loginedUser = this.getLoginedUser();
		BigInteger id = getParaToBigInteger("id", BigInteger.ZERO);
		Report report = ReportQuery.me().findById(id);
		setAttr("bean", report);
		setAttr("clerk_id", loginedUser.getId().toString());
		render("proc.html");
	}
	
	@PermAnnotation("report-proc")
	public void doproc(){
		final Report report = getModel(Report.class);
		report.update();
		setAttr("include", "_doproc_include.html");
		setAttr("href", this.getRequest().getHeader("referer"));
		setAttr("p", "report");
		render("doproc.html");
	}
}
