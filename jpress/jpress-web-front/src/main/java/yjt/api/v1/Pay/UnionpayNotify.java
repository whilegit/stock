package yjt.api.v1.Pay;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StrKit;
import com.unionpay.acp.sdk.LogUtil;

import io.jpress.core.BaseFrontController;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import yjt.Utils;
import yjt.api.v1.Push.ChargeFailPush;
import yjt.api.v1.UnionAppPay.UnionAppPay;
import yjt.core.push.Push;
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
				User user = UserQuery.me().findByIdNoCache(unionpayLog.getUserId());
				int status = unionpayLog.getStatus();
				if(status == 0) {
					boolean success = false;
					Date createTime = unionpayLog.getCreateTime();
					if(txnTime.equals(Utils.getDayNumber(createTime))){
						//交易确实成功，更新数据库
						if(user != null) {
							unionpayLog.setStatus(1);
							unionpayLog.setUpdateTime(new Date());
							unionpayLog.update();
							if(orderId.startsWith("PA")) {
								LogUtil.writeLog("银联充值，用户 " + user.getId().toString() + "增加余额 " + unionpayLog.getFee() + "元");
								user.changeBalance(unionpayLog.getFee(), "银联充值", BigInteger.ZERO, CreditLog.Platfrom.UNIONPAY);
							} else if(orderId.startsWith("PM")){
								LogUtil.writeLog("银联支付(用于手机号认证)，用户 " + user.getId().toString() + " 支付 " + unionpayLog.getFee() + "元认证费");
								user.changeBalance(unionpayLog.getFee(), "银联充值", BigInteger.ZERO, CreditLog.Platfrom.UNIONPAY);
								user.changeBalance(-unionpayLog.getFee(), "手机号认证费扣款", BigInteger.ZERO, CreditLog.Platfrom.JIETIAO365);
							}
							success = true;
						} else {
							LogUtil.writeLog("银联通知交易成功，但该笔支付的用户却不存在, paySn=" + orderId + ", txtTime=" + txnTime);
						} 
					} else {
						LogUtil.writeLog("银联通知交易成功，数据库找到匹配的支付记录号，但支付时间不匹配, paySn=" + orderId + ", txtTime=" + txnTime);
					}
					
					//充值不成功
					if(success == false && orderId.startsWith("PA") && user != null) {
						String deviceToken = user.getDeviceToken();
						if(StrKit.notBlank(deviceToken)) {
							ChargeFailPush cfp = new ChargeFailPush(deviceToken, new Date(), unionpayLog.getFee());
							Push.send(cfp);
						}
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
