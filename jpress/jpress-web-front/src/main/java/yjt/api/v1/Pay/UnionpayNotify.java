package yjt.api.v1.Pay;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.unionpay.acp.sdk.LogUtil;

import io.jpress.core.BaseFrontController;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import yjt.Utils;
import yjt.api.v1.UnionAppPay.UnionAppPay;
import yjt.model.CreditLog;
import yjt.model.UnionpayLog;
import yjt.model.query.UnionpayLogQuery;

@RouterMapping(url="/v1/pay")
public class UnionpayNotify extends BaseFrontController{
	
	public void unionpay() {
		UnionAppPay unionAppPay = new UnionAppPay();
		HttpServletRequest req = this.getRequest();
		Map<String, String> params = unionAppPay.back_notify(req);
		if(params != null) {
			String orderId =params.get("orderId");
			String txnTime = params.get("txnTime");
			UnionpayLog unionpayLog = UnionpayLogQuery.me().findByPaySn(orderId);
			if(unionpayLog != null) {
				int status = unionpayLog.getStatus();
				if(status == 0) {
					Date createTime = unionpayLog.getCreateTime();
					if(txnTime.equals(Utils.getDayNumber(createTime))){
						//交易确实成功，更新数据库
						User user = UserQuery.me().findByIdNoCache(unionpayLog.getUserId());
						if(user != null) {
							LogUtil.writeLog("银联通知交易成功，用户 " + user.getId().toString() + "增加金额 " + unionpayLog.getFee() + "元");

							unionpayLog.setStatus(1);
							unionpayLog.setUpdateTime(new Date());
							unionpayLog.update();
							
							user.changeBalance(unionpayLog.getFee(), "银联App控件版充值转入", BigInteger.ZERO, CreditLog.Platfrom.UNIONPAY);
							
						} else {
							LogUtil.writeLog("银联通知交易成功，但该笔支付的用户却不存在, paySn=" + orderId + ", txtTime=" + txnTime);
						} 
					} else {
						LogUtil.writeLog("银联通知交易成功，数据库找到匹配的支付记录号，但支付时间不匹配, paySn=" + orderId + ", txtTime=" + txnTime);
					}
				} else {
					//重复通知，不处理
				}
			} else {
				LogUtil.writeLog("银联通知交易成功，但数据库未找到匹配的支付记录号, paySn=" + orderId);
			}
			
		}
		this.renderText("ok");
	}
}
