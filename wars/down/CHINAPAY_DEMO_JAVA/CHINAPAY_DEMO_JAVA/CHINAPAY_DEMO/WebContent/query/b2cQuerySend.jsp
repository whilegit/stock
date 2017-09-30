<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="./css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>b2c demo 查询交易</title>
</head>
<body>
<%
	String pay_url = request.getContextPath()+"/queryServlet.do";
%>
<form name="payment" action="<%= pay_url %>" method="POST" target="_blank">
	<table border="1" cellpadding="2" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111">
		<tr>
			<td>
				<font color=red>*</font>商户号
			</td>
	
			<td>
	            ${MerId }
	        </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>订单号
			</td>
	
			<td>
	                   ${MerOrderNo }
	        </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>交易日期
			</td>
	
			<td>
	                   ${TranDate }
	        </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>交易时间
			</td>
	
			<td>
	                   ${TranTime } 
	        </td>
		</tr>
		<tr>
			<td>
				<font color=red>*</font>交易类型
			</td>
	
			<td>
	                   ${TranType } 
	        </td>
		</tr>
		
		<tr>
			<td>
				<font color=red>*</font>业务类型
			</td>
	
			<td>
	                   ${BusiType }
	           </td>
		</tr>
		
		
		<tr>
			<td>
				<font color=red>*</font>版本号
			</td>
	
			<td>
	                  ${Version}
	           </td>
		</tr>
		<tr>
			<td>
				交易扩展域
			</td>
	
			<td>
	                  ${TranReserved } 
	        </td>
		</tr>
		<tr>
			<td>
				签名信息
			</td>
	
			<td>
	                  ${Signature} 
	        </td>
		</tr>
	</table>
	<hr>
<%
	Enumeration<String> requestAttributeNames = request.getAttributeNames();
	//商户发送的报文key值
	String params = "BusiType;MerId;MerOrderNo;Signature;TranDate;TranType;TranReserved;Version;TranTime";
	while(requestAttributeNames.hasMoreElements()){
		String name = requestAttributeNames.nextElement();
		if(params.contains(name)){
			String value = (String)request.getAttribute(name);
	%>	
			<input type="hidden" name = '<%=name %>' value ='<%=value%>'/>
	<%		}	
		}
%>
	如果您的浏览器没有弹出支付页面，请点击按钮<input type='button' value='提交订单' onClick='document.payment.submit()'>再次提交。
</form>

</body>
</html>