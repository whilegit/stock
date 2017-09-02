/**
 * Author: lzr
 */
package yjt.model;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

import io.jpress.model.User;
import io.jpress.model.core.Table;
import io.jpress.model.query.UserQuery;
import yjt.Utils;
import yjt.model.base.BaseContract;
import yjt.model.query.ContractQuery;

@Table(tableName = "contract", primaryKey = "id")
public class Contract extends BaseContract<Contract>{

	protected static final Log log = Log.getLog(Contract.class);
	private static final long serialVersionUID = 1L;
	
	private User debitUser;  	//借款人
	private User creditUser; 	//贷款人
	private Status enumStatus;  //合约状态
	
	public User getDebitUser(){
		if(debitUser != null) return debitUser;
		if (getDebitId() == null) return null;

		debitUser = UserQuery.me().findById(getDebitId());
		return debitUser;
	}
	
	public User getCreditUser(){
		if(creditUser != null) return creditUser;
		if(getCreditId() == null) return null;
		creditUser = UserQuery.me().findById(getCreditId());
		return creditUser;
	}
	
	public String getStatusDesc(){
		if(enumStatus != null) System.out.println(JSONObject.toJSONString(enumStatus));
		if(enumStatus != null) return enumStatus.getName();
		enumStatus = Status.getEnum(getStatus());
		return enumStatus.getName();
	}
	
	public static RepaymentMethod getDefaultReportMethod() {
		return RepaymentMethod.WHOLE_AT_MATURITY;
	}
	
	//当前合约的版本号为1，留作扩展时用
	public static int getDefaultContractTypeId() {
		return 1;
	}
	
	protected static String genContractNumber_phy(Date now) {
		String ymdhms = Utils.getDayNumber(now);
		return "CN" + ymdhms + String.format("%06d", Utils.random(1, 999999));
	}
	
	public static String genContractNumber(Date now) {
		String contractNumberTmp = "";
		String contractNumber = null;
		for(int i = 0; i<10; i++) {
			contractNumberTmp = genContractNumber_phy(now);
			if(ContractQuery.me().isContractNumberExists(contractNumberTmp) == false) {
				contractNumber = contractNumberTmp;
				break;
			}
		}
		return contractNumber;
	}
	
	public static JSONObject createContract(Apply apply, User creditor, User debitor, RepaymentMethod repaymentMethod) {
		JSONObject ret = new JSONObject();
		ret.put("err", null);
		Date now = new Date();
		Contract contract = new Contract();
		contract.setAmount(apply.getAmount().doubleValue());
		contract.setAnnualRate(apply.getAnnualRate().doubleValue());
		contract.setCreateTime(now);
		contract.setCreditId(creditor.getId());
		contract.setDebitId(apply.getApplyUid());
		contract.setMaturityDate(apply.getMaturityDate());
		contract.setRepaymentMethod(repaymentMethod != null ? repaymentMethod.getIndex() : getDefaultReportMethod().getIndex());
		//确定用户是否有直接借款的权利，若无则需后台核准
		if(creditor.getCanLend() == 1) {
			contract.setLoanTerm(Utils.days(now, apply.getMaturityDate()));
			contract.setStatus(Status.ESTABLISH.getIndex());
			contract.setRepaymentStatus(RepaymentStatus.Pass.getIndex());
			contract.setValueDate(now);
		}else {
			contract.setStatus(Status.INIT.getIndex());
			contract.setRepaymentStatus(RepaymentStatus.NA.getIndex());
		}
		String contractNumber = genContractNumber(now);
		if(StrKit.isBlank(contractNumber)) {  
			//实际上是连续10次生成的交易号都不是唯一的
			ret.put("err", "服务器忙，请稍后重试");
			return ret;
		}
		contract.setContractNumber(contractNumber);
		contract.setContractTypeId(getDefaultContractTypeId());
		boolean flag = contract.save();
		if(flag == false) {
			ret.put("err", "创建合约失败");
			return ret;
		}
		
		boolean isDeductBalance = false;
		boolean isAddBalance = false;
		double apply_amount = apply.getAmount().doubleValue();
		try {
			//设置申请已达成交易
			if(creditor.getCanLend() == 0) {
				apply.setStatus(Apply.Status.WAIT.getIndex());
			} else {
				apply.setStatus(Apply.Status.DEALED.getIndex());
				apply.setValueDate(now);
			}
			flag = apply.update();
			if(flag == false) {
				throw new Exception("申请状态更新失败");
			}
			//如果用户可以直接放款，则立即进行资转划转
			if(creditor.getCanLend() == 1) {
				isDeductBalance = creditor.changeBalance(-apply_amount, "作为贷方扣款，交易号： " + contract.getContractNumber());
				if(isDeductBalance == false) {
					throw new Exception("扣款失败");
				}
				
				isAddBalance = debitor.changeBalance(+apply_amount, "作为借方放款，交易号： " + contract.getContractNumber());
				if(isAddBalance == false) {
					throw new Exception("扣款失败");
				}
			}
		} catch (Exception e) {
			if(isDeductBalance == true) {
				creditor.changeBalance(+apply_amount, "作为贷方扣款冲回，交易号： " + contract.getContractNumber());
			}
			if(isAddBalance == true) {
				debitor.changeBalance(-apply_amount, "作为借方放款扣回，交易号： " + contract.getContractNumber());
			}
			contract.setStatus(Contract.Status.CLOSE.getIndex());   //失败将合约也解除
			contract.update();
			log.info("交易失败并关闭, 交易号 " + contract.getContractNumber());
			
			//恢复申请的状态
			apply.setStatus(Apply.Status.VALID.getIndex());
			apply.setValueDate(null);
			log.info("申请号重设为可交易状态, 申请号 " + apply.getId().toString() + ",相关交易编号 " + contract.getContractNumber());
			
			ret.put("err", e.getMessage());
			return ret;
		}
		log.info("交易成功, 交易编号 " +  contract.getContractNumber() + ", 申请号 " + apply.getId().toString());
		ret.put("contract", contract);
		return ret;
	}
	
	public JSONObject getProfile() {
		JSONObject json = new JSONObject();
		User debitor = this.getDebitUser();
		User creditor = this.getCreditUser();
		json.put("id", getId().toString());
		json.put("contractNumber", getContractNumber());
		json.put("repaymentMethod", this.getRepaymentMethod());
		json.put("contractTypeId", this.getContractTypeId());
		json.put("debitUid", debitor.getId().toString());
		json.put("debitName", debitor.getRealname());
		json.put("creditUid", creditor.getId().toString());
		json.put("creditName", creditor.getRealname());
		json.put("amount", Utils.bigDecimalRound2(getAmount()));
		json.put("annualRate", Utils.bigDecimalRound2(getAnnualRate()));
		json.put("repaymentMethod", ""+getRepaymentMethod());
		json.put("repaymentMethodChs", RepaymentMethod.getEnum(this.getRepaymentMethod()).getName());
		json.put("createTime", Utils.toYmdHms(this.getCreateTime()));
		json.put("valueDate", Utils.toYmd(this.getValueDate()));
		json.put("maturityDate", Utils.toYmd(this.getMaturityDate()));
		json.put("loanTerm", ""+Utils.days(getValueDate(), getMaturityDate()));
		json.put("status",""+getStatus());
		json.put("statusChs",Status.getEnum(this.getStatus()).getName());
		return json;
	}
	
	public double calcInterest() {
		double insteret = 0.0;
		int loan_term = Utils.days(getValueDate(), getMaturityDate());
		insteret = getAmount().doubleValue() * (Math.pow(getAnnualRate().doubleValue() / 100.0 + 1.00, ((double)loan_term) / 365.0) - 1.0);
		return insteret;
	}
	
	public static enum Status{
		//状态，0合约初订立（贷方资金冻结），1风控一批准，2风控二批准，3风控三批准，4资金划转前关闭，5资金划转成功贷款正式进入还款期，6正常结束，7展期, 8损失,
		INIT("合约初订立", 0), RISK1("风控1", 1), RISK2("风控2", 2), RISK3("风控3", 3), CLOSE("合约关闭", 4), 
		ESTABLISH("生效", 5), FINISH("正常结束", 6), EXTEND("展期", 7), LOST("损失", 8),ALL("全部",100);
		private String name;
		private int index;
	    private Status(String name, int index) {  
	        this.name = name;  
	        this.index = index;  
	    }
	    // 普通方法  
	    public static Status getEnum(int index){
	    	for (Status s : Status.values()) {  
	            if (s.getIndex() == index) {  
	                return s;  
	            }  
	        }
	        return null; 
	    }
	    
		public String getName() {	return name;	}
		public void setName(String name) {	this.name = name;	}
		public int getIndex() {	return index;	}
		public void setIndex(int index) {	this.index = index;	}
	   
	}
	
	//约定的还款方式，1按月等额本息，2按月等额本金，3到期还本付息(默认)
	public static enum RepaymentMethod{
		AVERAGE_CAPITAL_PLUS_INTEREST ("按月等额本息", 1), AVERAGE_CAPITAL("按月等额本金", 2), WHOLE_AT_MATURITY("到期还本付息", 3);
		private String name;
		private int index;
	    private RepaymentMethod(String name, int index) {  
	        this.name = name;  
	        this.index = index;  
	    }
	    // 普通方法  
	    public static RepaymentMethod getEnum(int index){
	    	for (RepaymentMethod s : RepaymentMethod.values()) {  
	            if (s.getIndex() == index) {  
	                return s;  
	            }  
	        }
	        return null; 
	    }
	    
		public String getName() {	return name;	}
		public void setName(String name) {	this.name = name;	}
		public int getIndex() {	return index;	}
		public void setIndex(int index) {	this.index = index;	}
	}
	
	//还款状态：0不适用，1付息正常，2付息关注，3付息次级，4付息可疑，5损失
	public static enum RepaymentStatus{
		NA ("不适用", 0),Pass ("付息正常", 1), SPECIAL_MENTION("关注", 2), SUB_STANDARD("次级", 3),
		DOUTFUL("可疑", 4), LOSS("损失", 5);
		private String name;
		private int index;
	    private RepaymentStatus(String name, int index) {  
	        this.name = name;  
	        this.index = index;  
	    }
	    // 普通方法  
	    public static RepaymentStatus getEnum(int index){
	    	for (RepaymentStatus s : RepaymentStatus.values()) {  
	            if (s.getIndex() == index) {  
	                return s;  
	            }  
	        }
	        return null; 
	    }
	    
		public String getName() {	return name;	}
		public void setName(String name) {	this.name = name;	}
		public int getIndex() {	return index;	}
		public void setIndex(int index) {	this.index = index;	}
	}

}
