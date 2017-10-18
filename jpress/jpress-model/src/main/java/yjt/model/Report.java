package yjt.model;

import java.math.BigInteger;

import com.jfinal.kit.StrKit;

import io.jpress.model.User;
import io.jpress.model.core.Table;
import io.jpress.model.query.UserQuery;
import yjt.Utils;
import yjt.model.base.BaseReport;

@Table(tableName = "report", primaryKey = "id")
public class Report extends BaseReport<Report>{

	private static final long serialVersionUID = 1L;
	
	public User getFromUser() {
		User u =  UserQuery.me().findByIdNoCache(getFromUserId());
		if( u == null) {
			u = new User();
		}
		return u;
	}
	
	public User getToUser() {
		BigInteger toUserId = getToUserId();
		User u = UserQuery.me().findByIdNoCache(toUserId);
		if( u == null) {
			u = new User();
		}
		return u;
	}
	
	public String getImgsHtml() {
		String ret = "";
		String imgs = getImgs();
		if(StrKit.notBlank(imgs)) {
			StringBuffer sb = new StringBuffer();
			String[] ary = imgs.split(",");
			for(String s : ary) {
				String url = Utils.toMedia(s);
				sb.append("<a href=\""+url+"\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\""+url+"\"></a> ");
			}
			ret = sb.toString();
		}
		return ret;
	}
	
	public static enum Status{
		BEFORE("未处理", 0), AFTER("已处理", 1);
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
