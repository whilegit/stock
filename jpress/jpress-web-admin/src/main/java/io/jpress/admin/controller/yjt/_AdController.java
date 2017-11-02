package io.jpress.admin.controller.yjt;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.core.perm.PermAnnotation;
import yjt.core.perm.PermKit;
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
	
	@PermAnnotation("ad-view")
	public void edit(){
		BigInteger id = getParaToBigInteger("id", BigInteger.ZERO);
		Ad ad = AdQuery.me().findById(id);
		if(ad == null){
			this.renderText("出错了，广告不存在");
			return;
		}
		boolean hasEditPerm = PermKit.permCheck("ad-edit", getLoginedUser());
		setAttr("hasEditPerm", hasEditPerm);
		setAttr("bean", ad);
		setAttr("include","_edit_include.html");
		render("edit.html");
	}
	
	@PermAnnotation("ad-edit")
	public void save(){
		HashMap<String, String> files = getUploadFilesMap();
		final Map<String, String> metas = getMetas(files);
		final Ad ad = getModel(Ad.class);
		if (files != null && files.containsKey("ad.img")) {
			ad.setImg(files.get("ad.img"));
		}
		ad.setCreateTime(new Date());
		ad.update();
		this.renderAjaxResultForSuccess();
	}
	
	@PermAnnotation("ad-add")
	public void add(){
		this.renderText("暂时不可用");
		return;
	}
	
	@PermAnnotation("ad-delete")
	public void delete(){
		this.renderText("暂时不可用");
		return;
	}
}
