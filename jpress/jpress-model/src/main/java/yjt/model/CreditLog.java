package yjt.model;

import io.jpress.model.core.Table;
import yjt.model.base.BaseCreditLog;

@Table(tableName = "creditlog", primaryKey = "id")
public class CreditLog extends BaseCreditLog<CreditLog>{

	private static final long serialVersionUID = 1L;
	
	//约定的还款方式，1按月等额本息，2按月等额本金，3到期还本付息(默认)
		public static enum Platfrom{
			UNKNOWN("unknown", 0), WEIXIN ("weixin", 1), ALIPAY("alipay", 2), UNIONPAY("unionpay", 3), JIETIAO365("365借条", 4);
			private String name;
			private int index;
		    private Platfrom(String name, int index) {  
		        this.name = name;  
		        this.index = index;  
		    }
		    // 普通方法  
		    public static Platfrom getEnum(int index){
		    	for (Platfrom s : Platfrom.values()) {  
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
