package yjt.model;


import io.jpress.model.User;
import io.jpress.model.core.Table;
import io.jpress.model.query.UserQuery;
import yjt.model.base.BaseWithdraw;

@Table(tableName = "withdraw", primaryKey = "id")
public class Withdraw extends BaseWithdraw<Withdraw>{

	private static final long serialVersionUID = 1L;
	
	public User getUser() {
		return UserQuery.me().findById(this.getUserId());
	}
	
	public static enum PayType{
		UNKNOWN("未知", 0), WEIXIN("微信", 1), ALIPAY("支付宝", 2), UNIONPAY("银联", 3);
		private String name;
		private int index;
	    private PayType(String name, int index) {  
	        this.name = name;  
	        this.index = index;  
	    }
	    // 普通方法  
	    public static PayType getEnum(int index){
	    	for (PayType s : PayType.values()) {  
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
	
	public static enum Status{
		UNTRANS("未打款", 0), TRANS("已打款", 1), FAIL("失败", 2);
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

