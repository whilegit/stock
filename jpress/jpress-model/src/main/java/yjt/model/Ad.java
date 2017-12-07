package yjt.model;

import io.jpress.model.core.Table;
import yjt.Utils;
import yjt.model.base.BaseAd;

@Table(tableName = "ad", primaryKey = "id")
public class Ad extends BaseAd<Ad>{

	private static final long serialVersionUID = 1L;
	
	public String getImgMedia() {
		return Utils.toMedia(getImg());
	}
	
	public String getUrlMedia() {
		return Utils.toMedia(getUrl());
	}
	
	public static enum Status{
		INVALID("失效", 0), VALID("有效", 1);
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
