package io.jpress.admin.controller.yjt;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.core.perm.PermAnnotation;
import yjt.model.Contract;
import yjt.model.query.ContractQuery;

@RouterMapping(url = "/admin/contract", viewPath = "/WEB-INF/admin/contract")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _ContractController extends JBaseCRUDController<Contract>{
	
	@PermAnnotation("contract-list")
	public void index(){
		long total_records = ContractQuery.me().findCountByStatus(Contract.Status.ALL);
		setAttr("total_records", total_records);
		int page_index = (getPara("page") != null) ? Integer.parseInt(getPara("page")) : 1;
		setAttr("page_index", page_index);
		int page_size = (getPara("pagesize") != null) ? Integer.parseInt(getPara("pagesize")) : 10;
		setAttr("page_size", page_size);
		int total_pages = (int)Math.ceil(((double)total_records)/page_size);
		setAttr("total_pages", total_pages);
		List<Contract> list = ContractQuery.me().findList(page_index, page_size, null);
		setAttr("include", "_index_include.html");
		setAttr("list", list);
		render("index.html");
	}
	
	@PermAnnotation("contract-stat")
	public void stat(){
		this.renderText("功能暂时未完成");
	}
}
