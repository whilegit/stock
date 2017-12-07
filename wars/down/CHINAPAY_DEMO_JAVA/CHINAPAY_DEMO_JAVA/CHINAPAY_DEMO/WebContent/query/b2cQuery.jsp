<%@page import="java.util.Random"%>
<%@page import="java.util.Date"%>
<%@page import="com.chinapay.util.StringUtil"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>B2C_DEMO 查询交易</title>
</head>

<body>
<br>

<%
	//提供默认值
	String merId = "";
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
				<font color=red>*</font>订单号
			</td>

			<td>
                    <input type="text" name="MerOrderNo" value="<%= orderNo %>" maxlength="16"> &nbsp;(16位数字,必填字段，且当天不能重复)
            </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>交易日期
			</td>

			<td>
                    <input type="text" name="TranDate" value="<%= tranDate %>" maxlength="8"> &nbsp;(8位数字，为订单提交日期)
            </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>交易时间
			</td>

			<td>
                    <input type="text" name="TranTime" value="<%= tranTime %>" maxlength="6"> &nbsp;(6位数字，为订单提交时间)
            </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>交易类型
			</td>

			<td>
                    <input type="text" name="TranType" value="0502" maxlength="4"> &nbsp;(4位数字，查询交易为0502)
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
		
<!-- 		<tr> -->
<!-- 			<td> -->
<!-- 				<font color=red>*</font>接入类型 -->
<!-- 			</td> -->

<!-- 			<td> -->
<!--                     <input type="text" name="AccessType" value="0" maxlength="1"> &nbsp;(1位数字，默认:0,表示接入类型[0:以商户身份接入；1:以机构身份接入]) -->
<!--             </td> -->
<!-- 		</tr> -->
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