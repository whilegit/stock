package yjt.model;

import java.math.BigInteger;

import io.jpress.model.User;
import io.jpress.model.core.Table;
import yjt.model.base.BaseFollow;

/**
 * @author lzr
 *
 */

@Table(tableName = "follow", primaryKey = "id")
public class Follow extends BaseFollow<Follow>{

	private static final long serialVersionUID = 1L;
	
	private User followed;  //被关注人
	private User follower; 	//关注人
	
	public static enum Status{
		UNFOLLOWED("未关注", 0), FOLLOWED("已关注", 1);
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
