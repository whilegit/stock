package io.jpress.admin.controller.yjt;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.core.perm.PermAnnotation;
import yjt.model.Feedback;
import yjt.model.query.FeedbackQuery;


@RouterMapping(url = "/admin/feedback", viewPath = "/WEB-INF/admin/feedback")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _FeedbackController extends JBaseCRUDController<Feedback>{
	
	@PermAnnotation("feedback-list")
	public void index(){	
		int count = (int) FeedbackQuery.me().findCount();
		setAttr("count", count);
		
		String keyword = getPara("k", "").trim();
		Page<Feedback> page =  FeedbackQuery.me().paginateBySearch(getPageNumber(), getPageSize(), keyword, null);
		setAttr("page", page);
		
		setAttr("include", "_index_include.html");
		render("index.html");
	}
}
