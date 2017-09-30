<%@page import="java.util.Random"%>
<%@page import="java.util.Date"%>
<%@page import="com.chinapay.util.StringUtil"%>
<%@page import="sun.swing.StringUIClientPropertyKey"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>B2C_DEMO 退款交易</title>
</head>

<body>
<br>

<%
	//提供默认值
	String merId = "000000000000001";
	String orderAmt = "1";
	//日期时间
	Calendar calendar = Calendar.getInstance();
	Date date = calendar.getTime();
	String tranDate = StringUtil.getRelevantDate(date);
	String tranTime = StringUtil.getRelevantTime(date);
	
	
	//生成订单号
	Random rm = new Random(); 
	String random = String.valueOf(rm.nextDouble()).substring(2);
	String orderNo = StringUtil.pad(random, "l", 16, "0");
	
	//支付版本号
	String version ="20140728";
	
	String host = "127.0.0.1";
	//前台接收地址
	String contextPath = request.getContextPath();
	String pageReturnUrl = "http://"+host+contextPath+"";
	//后台接收地址
	String bgReturnUrl = "http://"+host+contextPath+"";
%>
<form name="createOrder" action="<%=contextPath %>/signServlet.do" method="POST">
	<table>
		<tr>
			<td>
				<font color=red>*</font>商户号
			</td>

			<td>
                   <input type="text" name="MerId" value="<%=merId %>" maxlength="15"> &nbsp;(15位数字，由chinapay分配)
            </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>退款订单号
			</td>

			<td>
                    <input type="text" name="MerOrderNo" value="<%= orderNo %>" maxlength="16"> &nbsp;(16位数字,必填字段，且当天不能重复)
            </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>退款金额
			</td>

			<td>
                    <input type="text" name="RefundAmt" value="<%= orderAmt %>" maxlength="20"> &nbsp;(1-20位数字，不填默认金额为1分)
            </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>退款交易日期
			</td>

			<td>
                    <input type="text" name="TranDate" value="<%= tranDate %>" maxlength="8"> &nbsp;(8位数字，为订单提交日期)
            </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>退款交易时间
			</td>

			<td>
                    <input type="text" name="TranTime" value="<%= tranTime %>" maxlength="6"> &nbsp;(6位数字，为订单提交时间)
            </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>原支付交易订单号
			</td>

			<td>
                    <input type="text" name="OriOrderNo" value="<%= orderNo %>" maxlength="16"> &nbsp;(16位数字，为支付交易订单号)
            </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>原支付交易日期
			</td>

			<td>
                    <input type="text" name="OriTranDate" value="<%= tranDate %>" maxlength="8"> &nbsp;(8位数字，为订单提交时间)
            </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>交易类型
			</td>

			<td>
                    <input type="text" name="TranType" value="0401" maxlength="4"> &nbsp;(4位数字，网银支付退款交易为0401)
            </td>
		</tr>
		
		<tr>
			<td>
				<font color=red>*</font>业务类型
			</td>

			<td>
                    <input type="text" name="BusiType" value="0001" maxlength="4"> &nbsp;(4位数字，固定值：0001)
            </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>版本号
			</td>

			<td>
                    <input type="text" name="Version" value="<%= version %>" maxlength="8"> &nbsp;(8位数字，支付接口版本号)
            </td>
		</tr>
		
		<tr>
			<td>
				分账类型
			</td>

			<td>
                    <input type="text" name="SplitType" value="" maxlength="4"> &nbsp;(4位数字,不分账不填写此域；填写规则[0001:实时分账;0002:延时分账])
            </td>
		</tr>
		<tr>
			<td>
				分账方式
			</td>

			<td>
                    <input type="text" name="SplitMethod" value="" maxlength="1"> &nbsp;(1位数字,不分账不填写此域；填写规则[0:按金额分账;1:按比例分账])
            </td>
		</tr>
		<tr>
			<td>
				分账公式
			</td>

			<td>
                    <input type="text" name="MerSplitMsg" value="" maxlength="256"> &nbsp;(不分账不填写此域；填写规则[商户号或者费用类型^金额或者占用比例;商户号或者费用^金额或者占用比例])
            </td>
		</tr>
		<tr>
			<td>
				交易币种
			</td>

			<td>
                    <input type="text" name="CurryNo" value="CNY" maxlength="3"> &nbsp;(3位，默认为CNY 人民币)
            </td>
		</tr>
		
		<tr>
			<td>
				接入类型
			</td>

			<td>
                    <input type="text" name="AccessType" value="0" maxlength="1"> &nbsp;(1位数字，默认:0,表示接入类型[0:以商户身份接入；1:以机构身份接入])
            </td>
		</tr>
		<tr>
			<td>
				收单机构号
			</td>

			<td>
                    <input type="text" name="AcqCode" value="000000000000014" maxlength="15"> &nbsp;(15位数字 )
            </td>
		</tr>
		<tr>
			<td>
				后台应答接收URL
			</td>

			<td>
                    <input type="text" name="MerBgUrl" value="<%=bgReturnUrl %>" maxlength="80"> &nbsp;(不超过80字节，商户系统后台应答接受地址)
               </td>
		</tr>
		
		<tr>
			<td>
				商户私有域
			</td>

			<td>
                    <input type="text" name="MerResv" value="MerResv" maxlength="60"> &nbsp;(英文或数字，不超过60字节，ChinaPay将原样返回给商户系统该字段填入的数据)
               </td>
		</tr>
		<tr>
			<td>
				业务ID
			</td>

			<td>
                <input type="text" name="trans_BusiId" value="" maxlength="8"> &nbsp;(可以为空,需要在chinapay开通业务Id编号)
            </td>
		</tr>
		<tr>
			<td>
				参数1
			</td>

			<td>
                <input type="text" name="trans_P1" value="" maxlength="512"> &nbsp;(可以为空,商户传输此业务下的参数值)
            </td>
		</tr>
		<tr>
			<td>
				参数2
			</td>

			<td>
                <input type="text" name="trans_P2" value="" maxlength="512"> &nbsp;(可以为空,商户传输此业务下的参数值)
            </td>
		</tr>
		<tr>
			<td>
				参数3
			</td>

			<td>
                <input type="text" name="trans_P3" value="" maxlength="512"> &nbsp;(可以为空,商户传输此业务下的参数值)
            </td>
		</tr>
		<tr>
			<td>
				参数4
			</td>

			<td>
                <input type="text" name="trans_P4" value="" maxlength="512"> &nbsp;(可以为空,商户传输此业务下的参数值)
            </td>
		</tr>
		<tr>
			<td>
				参数5
			</td>

			<td>
                <input type="text" name="trans_P5" value="" maxlength="512"> &nbsp;(可以为空,商户传输此业务下的参数值)
            </td>
		</tr>
		<tr>
			<td>
				参数6
			</td>

			<td>
                <input type="text" name="trans_P6" value="" maxlength="512"> &nbsp;(可以为空,商户传输此业务下的参数值)
            </td>
		</tr>
		<tr>
			<td>
				参数7
			</td>

			<td>
                <input type="text" name="trans_P7" value="" maxlength="512"> &nbsp;(可以为空,商户传输此业务下的参数值)
            </td>
		</tr>
		<tr>
			<td>
				参数8
			</td>

			<td>
                <input type="text" name="trans_P8" value="" maxlength="512"> &nbsp;(可以为空,商户传输此业务下的参数值)
            </td>
		</tr>
		<tr>
			<td>
				参数9
			</td>

			<td>
                <input type="text" name="trans_P9" value="" maxlength="512"> &nbsp;(可以为空,商户传输此业务下的参数值)
            </td>
		</tr>
		<tr>
			<td>
				参数10
			</td>

			<td>
                <input type="text" name="trans_P10" value="" maxlength="512"> &nbsp;(可以为空,商户传输此业务下的参数值)
            </td>
		</tr>
	</table>
	<input type='button' value='提交订单' onClick='document.createOrder.submit()'>
</form>
</body>
</html>