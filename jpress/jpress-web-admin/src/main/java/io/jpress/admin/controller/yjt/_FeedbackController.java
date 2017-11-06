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
	
	@PermAnnotation("feedback-delete")
	public void delete(){
		BigInteger id = getParaToBigInteger("id");
		if (id == null) {
			renderAjaxResultForError();
			return;
		}
		Feedback fd = FeedbackQuery.me().findById(id);
		fd.delete();
	}
	
	@PermAnnotation("feedback-proc")
	public void proc() {
		User loginedUser = this.getLoginedUser();
		BigInteger id = getParaToBigInteger("id", BigInteger.ZERO);
		Feedback feedback = FeedbackQuery.me().findById(id);
		setAttr("bean", feedback);
		setAttr("clerk_id", loginedUser.getId().toString());
		render("proc.html");
	}
	
	@PermAnnotation("feedback-proc")
	public void doproc(){
		final Feedback fd = getModel(Feedback.class);
		fd.update();
		setAttr("include", "_doproc_include.html");
		setAttr("href", this.getRequest().getHeader("referer"));
		setAttr("p", "feedback");
		render("doproc.html");
	}
}
