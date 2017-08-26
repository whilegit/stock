package yjt.model;

import io.jpress.model.core.Table;
import yjt.model.base.BaseApply;

@Table(tableName = "apply", primaryKey = "id")
public class Apply extends BaseApply<Apply>{

	private static final long serialVersionUID = 1L;
	
	public static enum Status{
		INVALID("无效", 0), VALID("有效", 1);
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
	
	public static enum Purpose{
		Business("个人经营", 1), Consume("消费", 2), STARTUP("创业",3), TURNOVER("临时周转",4), SCHOOL("助学", 5), 
		DECORATION("家居装修", 6), TOUR("旅游", 7);
		private String name;
		private int index;
	    private Purpose(String name, int index) {  
	        this.name = name;  
	        this.index = index;  
	    }
	    // 普通方法  
	    public static Purpose getEnum(int index){
	    	for (Purpose p : Purpose.values()) {  
	            if (p.getIndex() == index) {  
	                return p;  
	            }  
	        }
	        return null; 
	    }
	    
	    public static Purpose getEnum(String str){
	    	for (Purpose p : Purpose.values()) {  
	            if (p.getName().equals(str)) {  
	                return p;  
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
