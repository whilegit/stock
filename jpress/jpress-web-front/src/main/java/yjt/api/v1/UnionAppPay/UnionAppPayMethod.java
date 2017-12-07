package yjt.api.v1.UnionAppPay;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

import com.jfinal.kit.StrKit;

import yjt.Utils;
import yjt.model.UnionpayLog;

public class UnionAppPayMethod {

	
	public static Result chargePay(BigInteger memberID, double fee, PayType type) {
		Result result = new Result();
		Date now = new Date();
		//生成一个唯一的支付序列号
		String paySn = null;
		if(type == PayType.CHARGE) paySn = UnionpayLog.genUniquePaySn(now);
		else paySn = UnionpayLog.genVerifyMobilePaySn(now);
		
		if(StrKit.isBlank(paySn)) {
			//实际上是连续十次无法生成一个唯一的序列化，只能放弃
			result.setErr("服务暂时不可用，请稍后重试");
			return result;
		}
		UnionAppPay pay = new UnionAppPay();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("txnAmt", String.format("%d", (int)(fee * 100)));  //银联接口单位是分
		params.put("orderId", paySn);
		params.put("txnTime", Utils.getDayNumber(now));
		HashMap<String, String> consumeResult = (HashMap<String, String>) pay.comsume(params);
		if(consumeResult == null) {
			//无法获取到预支付的tn码，应查看log
			result.setErr("服务暂时不可用，请稍后重试");
			return result;
		}
		String tn = consumeResult.get("tn");
		
		//插入充值记录表
		UnionpayLog unionpayLog =new UnionpayLog();
		unionpayLog.setFee(fee);
		unionpayLog.setCreateTime(now);
		unionpayLog.setPaySn(paySn);
		unionpayLog.setStatus(0);  //表示未支付
		unionpayLog.setTn(tn);
		unionpayLog.setUserId(memberID);
		unionpayLog.setUpdateTime(now);
		boolean flag = unionpayLog.save();
		if(flag == false) {
			//无法插入支付记录表中，可能表不存在
			result.setErr("服务暂时不可用，请稍后重试");
			return result;
		}
		
		result.setUnionpayLog(unionpayLog);
		return result;
	}
	
	//约定的还款方式，1按月等额本息，2按月等额本金，3到期还本付息(默认)
	public static enum PayType{
		CHARGE ("充值", 1), MOBILE_VERIFY("手机号认证", 2);
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
	
	public static class Result{
		UnionpayLog unionpayLog = null;
		String err = null;
		public UnionpayLog getUnionpayLog() {
			return unionpayLog;
		}
		public void setUnionpayLog(UnionpayLog unionpayLog) {
			this.unionpayLog = unionpayLog;
		}
		public String getErr() {
			return err;
		}
		public void setErr(String err) {
			this.err = err;
		}
	}
}
