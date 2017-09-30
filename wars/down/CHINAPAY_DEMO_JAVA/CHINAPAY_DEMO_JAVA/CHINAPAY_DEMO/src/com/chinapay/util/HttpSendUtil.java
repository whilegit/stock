package com.chinapay.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class HttpSendUtil {
	
	/**
	 * 超时时间 .
	 */
	private static int SOCKET_TIMEOUT = 120000;
	/**
	 * 连接超时时间 .
	 */
	private static int CONNECT_TIMEOUT = 120000;
	
	public static CloseableHttpResponse sendToOtherServer(String url,Map<String,Object> dataMap){
		
		CloseableHttpClient client =  HttpClients.custom().build();
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(SOCKET_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setAuthenticationEnabled(false)
				.build();
		
		
		HttpPost post = new HttpPost(url);
		post.setProtocolVersion(org.apache.http.HttpVersion.HTTP_1_1);
		post.setConfig(config);
		
		HttpEntity entity = null;
		List<NameValuePair> formpair = new ArrayList<NameValuePair>();
		{
			for (String str : dataMap.keySet().toArray(new String[dataMap.size()])) {
				formpair.add(new BasicNameValuePair(str,dataMap.get(str).toString()));
			}
		}

		entity = new UrlEncodedFormEntity(formpair, Consts.UTF_8);
		if (entity != null) {
			post.setEntity(entity);
		}
		try {
			return client.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
