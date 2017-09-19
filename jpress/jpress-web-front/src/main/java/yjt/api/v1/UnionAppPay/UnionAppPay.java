package yjt.api.v1.UnionAppPay;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StrKit;
import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.LogUtil;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKConstants;


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
	
	/**
	 * 验证银联的后台通知
	 * @param req
	 * @return 如返回null，则表明此通知不值得处理；若非空，则表示交易成功并应更新数据库
	 */
	public Map<String, String> back_notify(HttpServletRequest req){
		Map<String, String> ret = null;
		LogUtil.writeLog("BackRcvResponse接收后台通知开始");
		String encoding = req.getParameter(SDKConstants.param_encoding);
		// 获取银联通知服务器发送的后台通知参数
		Map<String, String> reqParam = getAllRequestParam(req);
		LogUtil.printRequestLog(reqParam);

		//重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
		if (!AcpService.validate(reqParam, encoding)) {
			LogUtil.writeLog("验证签名结果[失败].");
			//验签失败，需解决验签问题
		} else {
			LogUtil.writeLog("验证签名结果[成功].");
			//【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
			
			String respCode = reqParam.get("respCode");
			//判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
			if("00".equals(respCode)) {
				String orderId =reqParam.get("orderId"); //获取后台通知的数据，其他字段也可用类似方式获取
				String txnTime = reqParam.get("txnTime");
				boolean flag = query(orderId, txnTime);
				if(flag == true) {
					LogUtil.writeLog("验证签名结果[成功]，通知成功，交易查询也成功");
					ret = reqParam;
				} else {
					LogUtil.writeLog("验证签名结果[成功]，通知成功，但交易查询却失败");
				}
			} else {
				LogUtil.writeLog("验证签名成功,但交易失败：respCode=" + respCode);
			}
		}
		LogUtil.writeLog("BackRcvResponse接收后台通知结束");
		return ret;
	}
	
	public boolean query(String orderId, String txnTime) {
		
		boolean ret = false;
		String merId = SDKConfig.getConfig().getMerId();
		if(!StrKit.notBlank(merId, orderId, txnTime)) {
			LogUtil.writeLog("错误：merId, orderId, txnTime中有参数为空");
			return false;
		}
		
		Map<String, String> data = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", SDKConfig.getConfig().getVersion());                 //版本号
		data.put("encoding", ENCODING);          //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
		data.put("txnType", "00");                             //交易类型 00-默认
		data.put("txnSubType", "00");                          //交易子类型  默认00
		data.put("bizType", "000201");                         //业务类型 
		
		/***商户接入参数***/
		data.put("merId", merId);                  			   //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改
		
		/***要调通交易以下字段必须修改***/
		data.put("orderId", orderId);                 			//****商户订单号，每次发交易测试需修改为被查询的交易的订单号
		data.put("txnTime", txnTime);                			//****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

		/**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
		
		Map<String, String> reqData = AcpService.sign(data,ENCODING);			//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String url = SDKConfig.getConfig().getSingleQueryUrl();								//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
		Map<String, String> rspData = AcpService.post(reqData, url,ENCODING); //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		
		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, ENCODING)){
				LogUtil.writeLog("验证签名成功");
				if(("00").equals(rspData.get("respCode"))){//如果查询交易成功
					String origRespCode = rspData.get("origRespCode");
					if(("00").equals(origRespCode)){
						//交易成功，更新商户订单状态
						ret = true;
					}else if(("03").equals(origRespCode)||
							 ("04").equals(origRespCode)||
							 ("05").equals(origRespCode)){
						//订单处理中或交易状态未明，需稍后发起交易状态查询交易 【如果最终尚未确定交易是否成功请以对账文件为准】
						//TODO
					}else{
						//其他应答码为交易失败
						//TODO
					}
				}else if(("34").equals(rspData.get("respCode"))){
					//订单不存在，可认为交易状态未明，需要稍后发起交易状态查询，或依据对账结果为准
					
				}else{//查询交易本身失败，如应答码10/11检查查询报文是否正确
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
	
	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				//在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				//System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
}
