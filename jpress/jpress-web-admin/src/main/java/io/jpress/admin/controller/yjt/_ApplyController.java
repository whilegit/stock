package io.jpress.admin.controller.yjt;

import java.math.BigInteger;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import io.jpress.utils.StringUtils;
import yjt.core.perm.PermAnnotation;
import yjt.model.Apply;
import yjt.model.query.ApplyQuery;

@RouterMapping(url = "/admin/apply", viewPath = "/WEB-INF/admin/apply")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _ApplyController extends JBaseCRUDController<Apply>{
	private String getModuleName() {
		return getPara("m");
	}

	private String getStatus() {
		return getPara("s");
	}
	
	@PermAnnotation("apply-list")
	public void index(){
		/*
		int count = (int) ContractQuery.me().findCount(Contract.Status.ALL, null, null, null);
		setAttr("count", count);
		int establish_count = (int) ContractQuery.me().findCount(Contract.Status.ESTABLISH, null, null, null);
		setAttr("establish_count", establish_count);
		int finish_count = (int) ContractQuery.me().findCount(Contract.Status.FINISH, null, null, null); 
		setAttr("finish_count", finish_count);
		int extend_count = (int) ContractQuery.me().findCount(Contract.Status.EXTEND, null, null, null);
		setAttr("extend_count", extend_count);
		int lost_count = (int) ContractQuery.me().findCount(Contract.Status.LOST, null, null, null);
		setAttr("lost_count", lost_count);
		
		String keyword = getPara("k", "").trim();
		
		Page<Contract> page = null;
		page = ContractQuery.me().paginateBySearch(getPageNumber(), getPageSize(), keyword,getStatus());
		setAttr("page", page);

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
		*/
		setAttr("StatusInvalidCode", ApplyQuery.Status.INVALID.getIndex());	
		setAttr("StatusValidCode", ApplyQuery.Status.VALID.getIndex());
		setAttr("StatusDealedCode", ApplyQuery.Status.DEALED.getIndex());
		setAttr("StatusTimeoutCode", ApplyQuery.Status.TIMEOUT.getIndex());
		
		int count = (int) ApplyQuery.me().findCount(null);
		setAttr("count", count);
		int dealed_count = (int) ApplyQuery.me().findCount(ApplyQuery.Status.DEALED);
		setAttr("dealed_count", dealed_count);
		int valid_count =  (int) ApplyQuery.me().findCount(ApplyQuery.Status.VALID);
		setAttr("valid_count", valid_count);
		int timeout_count =  (int) ApplyQuery.me().findCount(ApplyQuery.Status.TIMEOUT);
		setAttr("timeout_count", timeout_count);
		int invalid_count =  (int) ApplyQuery.me().findCount(ApplyQuery.Status.INVALID);
		setAttr("invalid_count", invalid_count);
		
		String keyword = getPara("k", "").trim();
		Page<Apply> page = null;
		page = ApplyQuery.me().paginateBySearch(getPageNumber(), getPageSize(), keyword, getStatus());
		setAttr("page", page);
		
		setAttr("include", "_index_include.html");
		render("index.html");
	}
	
	/*
	@PermAnnotation("contract-edit")
	public void edit(){
		int cid =  (getPara("cid") != null) ? Integer.parseInt(getPara("cid")) : 0;
		Contract contract = ContractQuery.me().findById(BigInteger.valueOf(cid));
		if(contract == null) {
			this.renderError(-1);
			return;
		}
		setAttr("contract", contract);
		setAttr("include", "_edit_include.html");
		render("edit.html");
	}
	*/
	
	@PermAnnotation("contract-stat")
	public void stat(){
		this.renderText("功能暂时未完成");
	}
}
