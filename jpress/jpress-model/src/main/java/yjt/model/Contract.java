/**
 * Author: lzr
 */
package yjt.model;

import java.math.BigInteger;

import com.alibaba.fastjson.JSONObject;

import io.jpress.model.User;
import io.jpress.model.core.Table;
import io.jpress.model.query.UserQuery;
import yjt.model.base.BaseContract;

@Table(tableName = "contract", primaryKey = "id")
public class Contract extends BaseContract<Contract>{

	private static final long serialVersionUID = 1L;
	
	private User debitUser;  	//借款人
	private User creditUser; 	//贷款人
	private Status enumStatus;  //合约状态
	
	public User getDebitUser(){
		if(debitUser != null) return debitUser;
		if (getDebitId() == null) return null;

		debitUser = UserQuery.me().findById(BigInteger.valueOf(getDebitId()));
		return debitUser;
	}
	
	public User getCreditUser(){
		if(creditUser != null) return creditUser;
		if(getCreditId() == null) return null;
		creditUser = UserQuery.me().findById(BigInteger.valueOf(getCreditId()));
		return creditUser;
	}
	
	public String getStatusDesc(){
		if(enumStatus != null) System.out.println(JSONObject.toJSONString(enumStatus));
		if(enumStatus != null) return enumStatus.getName();
		enumStatus = Status.getEnum(getStatus());
		return enumStatus.getName();
	}
	
	
	public static enum Status{
		//状态，0合约初订立（贷方资金冻结），1风控一批准，2风控二批准，3风控三批准，4资金划转前关闭，5资金划转成功贷款正式进入还款期，6正常结束，7展期, 8损失,
		INIT("合约初订立", 0), RISK1("风控1", 1), RISK2("风控2", 3), RISK3("风控3", 3), CLOSE("合约关闭", 4), 
		ESTABLISH("生效", 5), FINISH("正常结束", 6), EXTEND("展期", 7), LOST("损失", 8);
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

}
