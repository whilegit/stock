package yjt.model;

import io.jpress.model.core.Table;
import yjt.model.base.BaseReport;

@Table(tableName = "report", primaryKey = "id")
public class Report extends BaseReport<Report>{

	private static final long serialVersionUID = 1L;
	
	public static enum Status{
		INVALID("未处理", 0), VALID("已处理", 1);
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
