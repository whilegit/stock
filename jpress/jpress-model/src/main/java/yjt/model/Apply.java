package yjt.model;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.log.Log;

import io.jpress.model.User;
import io.jpress.model.core.Table;
import io.jpress.model.query.UserQuery;
import yjt.Utils;
import yjt.model.base.BaseApply;

@Table(tableName = "apply", primaryKey = "id")
public class Apply extends BaseApply<Apply>{

	private static final long serialVersionUID = 1L;
	protected static final Log log = Log.getLog(Apply.class);
	
	public User getApplyUser(){
		BigInteger userId = this.getApplyUid();
		return UserQuery.me().findById(userId);
	}
	
	public JSONObject getProfile() {
		JSONObject json = new JSONObject();
		User user = getApplyUser();
		json.put("id", getApplyUid().toString());
		json.put("userID", user.getId().toString());
		json.put("userName", user.getRealname());
		json.put("userAvatar", user.getAvatar());
		json.put("userOverdue", ""+user.getOverdue());
		long day = Utils.days(new Date(), getMaturityDate());
		json.put("day", day>0 ? day+"" : "0");  //因为申请时没有固定借款日，此处只能计算未来到今日的天数了
		json.put("money", Utils.bigDecimalRound2(getAmount()));
		json.put("rate", Utils.bigDecimalRound2(getAnnualRate()) + "%");
		json.put("endDate", Utils.toYmd(getMaturityDate()));
		json.put("retType", "" + getRepaymentMethod());
		json.put("fromUserCount", ""+getToFriends().size());
		json.put("forUse", Apply.Purpose.getEnum(getPurpose()).getName());
		json.put("video",getVideo());
		json.put("status", ""+getStatus());
		return json;
	}
	
	public boolean isInFriendList(BigInteger uid) {
		List<BigInteger> list = this.getToFriends();
		return list.contains(uid);
	}
	
	public String canDeal() {
		String err = null;
		Status status = Status.getEnum(getStatus());
		if(status != Apply.Status.VALID) {
			switch(status) {
				case DEALED:
					err = "该申请已达成";
					break;
				case INVALID:
					err = "该申请已关闭";
					break;
				case WAIT:
					err = "该申请已被抢占";
					break;
				default:
					err = "不能识别的申请状态";
					break;
			}
		}
		return err;
	}
	
	public static enum Status{
		//WAIT状态表示：贷方无can_lend权限，其所达成的交易必须要经过后台核准。
		INVALID("无效", 0), VALID("有效", 1), DEALED("已达成交易", 2), WAIT("申请达成的交易等待核准",3);
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
