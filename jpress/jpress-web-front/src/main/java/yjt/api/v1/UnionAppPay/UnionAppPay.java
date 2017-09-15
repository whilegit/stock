package yjt.api.v1.UnionAppPay;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.LogUtil;
import com.unionpay.acp.sdk.SDKConfig;


public class UnionAppPay {
	
	protected static final String ENCODING = "UTF-8";
	public UnionAppPay(){
		//从classpath加载acp_sdk.properties文件
		//SDKConfig.getConfig().loadPropertiesFromSrc();  //已在 jpress-web-core/src/main/java/com/io/jpress/Config.java中加载
	}
	
	public Map<String, String> comsume(HashMap<String, String> orderInfo){
		Map<String, String> ret = null;
		if(orderInfo == null || orderInfo.containsKey("txnAmt") == false || 
				orderInfo.containsKey("orderId") == false || orderInfo.containsKey("txnTime") == false){
			//参数不全
			return null;
		}
		String txnAmt = orderInfo.get("txnAmt");
		String orderId = orderInfo.get("orderId");
		String txnTime = orderInfo.get("txnTime");
		if(!StrKit.notBlank(txnAmt, orderId, txnTime)){
			//参数中有为空的
			return null;
		}
		
		Map<String, String> contentData = new HashMap<String, String>();
		contentData.put("version", SDKConfig.getConfig().getVersion());            //版本号 全渠道默认值
		contentData.put("encoding", ENCODING);     //字符集编码 可以使用UTF-8,GBK两种方式
		contentData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
		contentData.put("txnType", "01");              		 	//交易类型 01:消费
		contentData.put("txnSubType", "01");           		 	//交易子类 01：消费
		contentData.put("bizType", "000201");          		 	//填写000201
		contentData.put("channelType", "08");          		 	//渠道类型 08手机

		/***商户接入参数***/
		contentData.put("merId", SDKConfig.getConfig().getMerId()); //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		contentData.put("accessType", "0");            		 	//接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
		contentData.put("orderId", orderId);        	 	    //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则	
		contentData.put("txnTime", txnTime);		 		    //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		contentData.put("accType", "01");					 	//账号类型 01：银行卡02：存折03：IC卡帐号类型(卡介质)
		contentData.put("txnAmt", txnAmt);						//交易金额 单位为分，不能带小数点
		contentData.put("currencyCode", "156");                 //境内商户固定 156 人民币
		
		//后台通知地址
		contentData.put("backUrl", SDKConfig.getConfig().getBackUrl());
		//前台通知地址，若无不提供
		String frontUrl = SDKConfig.getConfig().getBackUrl();
		if(StrKit.notBlank(frontUrl)) contentData.put("frontUrl", frontUrl);
		
		/**对请求参数进行签名并发送http post请求，接收同步应答报文**/
		Map<String, String> reqData = AcpService.sign(contentData, ENCODING);			//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();				//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
		Map<String, String> rspData = AcpService.post(reqData,requestAppUrl,ENCODING);  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, ENCODING)){
				LogUtil.writeLog("验证签名成功");
				String respCode = rspData.get("respCode") ;
				if(("00").equals(respCode) && StrKit.notBlank(rspData.get("tn"))){
					//成功,返回的数据可以提取tn号
					ret = rspData;
				}else{
					//其他应答码为失败请排查原因或做失败处理
					//TODO
				}
			}else{
				LogUtil.writeErrorLog("验证签名失败");
				//TODO 检查验证签名失败的原因
			}
		}else{
			//未返回正确的http状态
			LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
		}
		return ret;
	}
}
