<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>QueryFile.jsp</title>
  </head>
  
 <body>
  	   <center><strong><font size="3">���������ļ���ѯ</font></strong></center><br>
 	   <form name="QueryFile" action="./QueryFile" method="POST" >
  <table  border="0" cellspacing="1" cellpadding="1" width="500"  bordercolor="#2B91D5" align="center">
	  <tr>
		  <td>ԭʼ�ļ�����:</td>
		  <td><input type="text" style="width:150px;" name="TransDate" value=""></td>
	  </tr>		   
	  <tr>
		  <td>�ļ����κ�:</td>
		  <td><input type="text" style="width:150px;" name="MerSeqId" value=""></td>
	   </tr>	 	   	 
  </table>
    <br>
  	   <center>
	  	   <input type="submit" name="submitButton" value="�ύ" class="cmd"/>
	   </center>
	   <br>
       <center>
  	<a href="./index.jsp">����</a></center><br><br><br>
  	</form>
  	
  </body>
</html>
