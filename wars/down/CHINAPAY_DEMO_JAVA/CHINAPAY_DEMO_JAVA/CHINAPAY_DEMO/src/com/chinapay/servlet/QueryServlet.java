﻿package com.chinapay.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;

import com.chinapay.util.HttpSendUtil;
import com.chinapay.util.PathUtil;
import com.chinapay.util.SignUtil;
import com.chinapay.util.StringUtil;

/**
 * Servlet implementation class QueryServlet
 */
public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	private static final String VERIFY_KEY = "VERIFY_KEY";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		//解析报文
		Enumeration<String> requestNames = request.getParameterNames();
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		while(requestNames.hasMoreElements()){
			String name = requestNames.nextElement();
			String value = request.getParameter(name);
			queryMap.put(name, value);
		}
		String query_url = PathUtil.getValue("query_url");
		
		CloseableHttpResponse httpResonse = HttpSendUtil.sendToOtherServer(query_url, queryMap);
		if(null==httpResonse){
			//
			response.getWriter().write("发送失败");
		}else{
		  String respStr = StringUtil.parseResponseToStr(httpResonse);
		  Map<String, String> resultMap = StringUtil.paserStrtoMap(respStr);
		  
		  //
		  for(Map.Entry<String, String> entry:resultMap.entrySet()){
			  request.setAttribute(entry.getKey(), entry.getValue());
		  }

		  if(SignUtil.verify(resultMap)){
			resultMap.put(VERIFY_KEY, "success");
			request.setAttribute(VERIFY_KEY, "success");
		  }else{
			resultMap.put(VERIFY_KEY, "fail");
			request.setAttribute(VERIFY_KEY, "fail");
		  }
		}
		request.getRequestDispatcher("/query/b2cQueryResult.jsp").forward(request, response);
	}

}
