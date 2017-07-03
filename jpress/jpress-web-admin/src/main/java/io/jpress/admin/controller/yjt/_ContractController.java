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
		List<Contract> list = ContractQuery.me().findList(1, 10, null);
		this.renderText(JSONObject.toJSONString(list));
	}
	
	@PermAnnotation("contract-stat")
	public void stat(){
		this.renderText("功能暂时未完成");
	}
}
