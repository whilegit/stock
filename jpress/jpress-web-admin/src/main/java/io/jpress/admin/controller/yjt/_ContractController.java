package io.jpress.admin.controller.yjt;

import java.math.BigInteger;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import yjt.Utils;
import yjt.core.perm.PermAnnotation;
import yjt.model.Apply;
import yjt.model.Contract;
import yjt.model.query.ApplyQuery;
import yjt.model.query.ContractQuery;

@RouterMapping(url = "/admin/contract", viewPath = "/WEB-INF/admin/contract")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _ContractController extends JBaseCRUDController<Contract>{
	
	protected static final Log log = Log.getLog(_ContractController.class);

	
	private String getModuleName() {
		return getPara("m");
	}

	private String getStatus() {
		return getPara("s");
	}
	
	@PermAnnotation("contract-list")
	public void index(){
		Integer today = getParaToInt("today", 0);
		int count = 0;
		if(today == 0) {
			count = (int) ContractQuery.me().findCount(Contract.Status.ALL, null, null, null);
		} else {
			setAttr("today", 1);
			count = (int) ContractQuery.me().findCount(null, null, null, null, Utils.getTodayStartTime(), null);
		}
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
		page = ContractQuery.me().paginateBySearch(getPageNumber(), getPageSize(), keyword,getStatus(), today==1?Utils.getTodayStartTime():null, null);
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
		setAttr("include", "_index_include.html");
		render("index.html");
	}
	
	@PermAnnotation("contract-edit")
	public void edit(){
		BigInteger id =  this.getParaToBigInteger("id", BigInteger.ZERO);
		Contract contract = ContractQuery.me().findById(id);
		if(contract == null) {
			this.renderText("所查询的借条不存在");
			return;
		}
		setAttr("contract", contract);
		String agreement = contract.getAgreement();
		if(StrKit.notBlank(agreement)) {
			setAttr("agreement", agreement);
		}
		Apply apply = ApplyQuery.me().findById(contract.getApplyId());
		setAttr("apply", apply);
		render("edit.html");
	}
	
	@PermAnnotation("contract-video")
	public void video() {
		BigInteger id = this.getParaToBigInteger("id", BigInteger.ZERO);
		Contract contract = ContractQuery.me().findById(id);
		if(contract == null) {
			this.renderText("所查询的借条不存在");
			return;
		}
		Apply apply = ApplyQuery.me().findById(contract.getApplyId());
		if(apply == null) {
			this.renderText("所查询的借条不存在");
			log.error("contract("+contract.getApplyId().intValue()+") 所指的apply不存在");
			return;
		}
		
		if(StrKit.isBlank(apply.getVideo())) {
			this.renderText("该借条无视频");
			return;
		}
		String href = Utils.toMedia(apply.getVideo());
		if(StrKit.notBlank(href)) {
			setAttr("href", href);
		}
		setAttr("contract", contract);
		render("video.html");
	}
}
