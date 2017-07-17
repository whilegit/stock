package io.jpress.admin.controller.yjt;

import java.math.BigInteger;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
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
		//根据合约编号查询
		String contractNumber = (getPara("contract_number") != null) ? getPara("contract_number") : null;
		
		//根据合约状态查询
		Contract.Status cs = Contract.Status.ALL;
		int status =  (getPara("status") != null) ? Integer.parseInt(getPara("status")) : Contract.Status.ALL.getIndex();
		cs = Contract.Status.getEnum(status);

		//根据借款人查询
		BigInteger debitUid = null;
		String debitAccount = (getPara("debit_account") != null) ? getPara("debit_account") : null;
		if(StrKit.notBlank(debitAccount)){
			User debitUser = UserQuery.me().findUserByUsername(debitAccount);
			debitUid = (debitUser != null) ? debitUser.getId() : null;
		}
		
		//根据放款人查询
		BigInteger creditUid = null;
		String creditAccount = (getPara("credit_account") != null) ? getPara("credit_account") : null;
		if(StrKit.notBlank(creditAccount)){
			User creditUser = UserQuery.me().findUserByUsername(creditAccount);
			creditUid = (creditUser != null) ? creditUser.getId() : null;
		}

		//查询记录条数和内容，并据此计算分页显示
		long total_records = ContractQuery.me().findCount(cs,contractNumber, debitUid, creditUid);
		int page_index = (getPara("page") != null) ? Integer.parseInt(getPara("page")) : 1;
		int page_size = (getPara("pagesize") != null) ? Integer.parseInt(getPara("pagesize")) : 10;
		int total_pages = (int)Math.ceil(((double)total_records)/page_size);
		List<Contract> list = ContractQuery.me().findList(page_index, page_size, cs, contractNumber, debitUid, creditUid);
	
		//设定渲染内容
		setAttr("list", list);
		setAttr("total_records", total_records);
		setAttr("total_pages", total_pages);
		setAttr("page_size", page_size);
		setAttr("page_index", page_index);
		//设置几个查询条件
		setAttr("credit_account", creditAccount);
		setAttr("debit_account", debitAccount);
		setAttr("status", status);
		setAttr("contract_number", contractNumber);
		//设置状态码
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
		
		setAttr("include", "_index_include.html");
		render("index.html");
	}
	
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
	
	@PermAnnotation("contract-stat")
	public void stat(){
		this.renderText("功能暂时未完成");
	}
}
