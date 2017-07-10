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
		
		
		int status =  (getPara("status") != null) ? Integer.parseInt(getPara("status")) : Contract.Status.ALL.getIndex();
		Contract.Status cs = Contract.Status.getEnum(status);
		long total_records = ContractQuery.me().findCountByStatus(cs);
		setAttr("total_records", total_records);
		int page_index = (getPara("page") != null) ? Integer.parseInt(getPara("page")) : 1;
		setAttr("page_index", page_index);
		int page_size = (getPara("pagesize") != null) ? Integer.parseInt(getPara("pagesize")) : 10;
		setAttr("page_size", page_size);
		int total_pages = (int)Math.ceil(((double)total_records)/page_size);
		setAttr("total_pages", total_pages);
		
		List<Contract> list = ContractQuery.me().findList(page_index, page_size, cs);
		setAttr("include", "_index_include.html");
		setAttr("list", list);
		
		setAttr("status", status);
		setAttr("StatusINITCode", Contract.Status.INIT.getIndex());		setAttr("StatusINITName", Contract.Status.INIT.getName());
		setAttr("StatusRISK1Code", Contract.Status.RISK1.getIndex());	setAttr("StatusRISK1Name", Contract.Status.RISK1.getName());
		setAttr("StatusRISK2Code", Contract.Status.RISK2.getIndex());	setAttr("StatusRISK2Name", Contract.Status.RISK2.getName());
		setAttr("StatusRISK3Code", Contract.Status.RISK3.getIndex());	setAttr("StatusRISK3Name", Contract.Status.RISK3.getName());
		setAttr("StatusCLOSECode", Contract.Status.CLOSE.getIndex());	setAttr("StatusCLOSEName", Contract.Status.CLOSE.getName());
		setAttr("StatusESTABLISHCode", Contract.Status.ESTABLISH.getIndex());	setAttr("StatusESTABLISHName", Contract.Status.ESTABLISH.getName());
		setAttr("StatusFINISHCode", Contract.Status.FINISH.getIndex());	setAttr("StatusFINISHName", Contract.Status.FINISH.getName());
		setAttr("StatusEXTENDCode", Contract.Status.EXTEND.getIndex());	setAttr("StatusEXTENDName", Contract.Status.EXTEND.getName());
		setAttr("StatusLOSTCode", Contract.Status.LOST.getIndex());		setAttr("StatusLOSTName", Contract.Status.LOST.getName());
		setAttr("StatusALLCode", Contract.Status.ALL.getIndex());		setAttr("StatusALLName", Contract.Status.ALL.getName());
		render("index.html");
	}
	
	@PermAnnotation("contract-stat")
	public void stat(){
		this.renderText("功能暂时未完成");
	}
}
