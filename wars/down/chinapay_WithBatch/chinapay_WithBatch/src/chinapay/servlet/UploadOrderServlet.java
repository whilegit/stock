package chinapay.servlet;

/**
 * 批量文件上传
 * @author huang.xuting
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import chinapay.bean.TransactionBean;
import chinapay.util.Config;
import chinapay.util.DigestMD5;
import chinapay.util.MsgUtil;

@SuppressWarnings("serial")
public class UploadOrderServlet extends HttpServlet {
	public int tmpResultLength = -1;
	private static final String KEY_CHINAPAY_MERID = "chinapay.merid";
	private static final String KEY_CHINAPAY_MERKEY_FILEPATH = "chinapay.merkey.filepath";
	private static final String KEY_CHINAPAY_PUBKEY_FILEPATH = "chinapay.pubkey.filepath";
	private static final String PaymentUrl = "chinapay.WithBatch.url";
	private static final String FilePath = "chinapay.BatchContent.filepath";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String merId = null;
		String MerKeyPath = null;
		String PubKeyPath = null;
		Properties config = Config.getInstance().getProperties();
		merId = config.getProperty(KEY_CHINAPAY_MERID);
		MerKeyPath = config.getProperty(KEY_CHINAPAY_MERKEY_FILEPATH);
		PubKeyPath = config.getProperty(KEY_CHINAPAY_PUBKEY_FILEPATH);
		String url = config.getProperty(PaymentUrl);
		String filePath = config.getProperty(FilePath);

		request.setCharacterEncoding("UTF-8");
		String fileName = request.getParameter("fileName");
		String fileContent = request.getParameter("fileContent");
		System.out.println(fileContent);
		String filepath = filePath + fileName;
		System.out.println(filepath);
			
		// 将批量代扣信息写入临时文件
		File file = new File(filepath);
		FileOutputStream fos = null;
		fos = new FileOutputStream(filepath);
		fos.write(fileContent.getBytes("UTF-8"));
		fos.flush();
		fos.close();
		
		// 文件上传准备
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		BufferedReader reader = null;
		InputStream resInputStream = null;
		try {
			httpClient = new HttpClient();
			httpClient.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			postMethod = new PostMethod(url);

			byte[] temSen = null;
			try {
				temSen = MsgUtil.getBytes(file);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int temSenLength = temSen.length;
			System.out.println("temSen=[" + temSenLength + "]");
			String tian = new String(temSen, "UTF-8");
			System.out.println("tian=[" + tian + "]");

			// 对需要上传的字段签名
			String chkValue2 = null;
			chkValue2 = DigestMD5.MD5Sign(merId, fileName, fileContent.getBytes("UTF-8"), MerKeyPath);
			System.out.println("批量文件上传接口签名内容:" + chkValue2);

			httpClient.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			// 获得管理参数
			HttpConnectionManagerParams managerParams = httpClient
					.getHttpConnectionManager().getParams();
			// 设置连接超时时间(单位毫秒)
			managerParams.setConnectionTimeout(40000);
			// 设置读数据超时时间(单位毫秒)
			managerParams.setSoTimeout(120000);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(1, false));
			NameValuePair[] data = { new NameValuePair("merId", merId),
					new NameValuePair("fileName", fileName),
					new NameValuePair("fileContent", tian),
					new NameValuePair("chkValue", chkValue2) };

			postMethod.setRequestBody(data);

			int statusCode = 0;
			try {
				statusCode = httpClient.executeMethod(postMethod);
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				resInputStream = postMethod.getResponseBodyAsStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// 接收返回报文
			reader = new BufferedReader(new InputStreamReader(resInputStream, "UTF-8"));
			String tempBf = null;
			StringBuffer html = new StringBuffer();
			while ((tempBf = reader.readLine()) != null) {

				html.append(tempBf);
			}
			String result = html.toString();
			System.out.println("批量文件上传接口返回报文result=[" + result + "]");

			// 拆分应答报文数据
			int dex = result.lastIndexOf("=");
			String tiakong = result.substring(0, dex + 1);
			System.out.println("验签明文：" + "[" + tiakong + "]");
			String ChkValue = result.substring(dex + 1);
			
			String str[] = result.split("&");
			for(int i = 0; i<str.length; i++){
				System.out.println("-----------------------------"+str[i]);
			}
			int Res_Code = str[0].indexOf("=");
			int Res_message = str[1].indexOf("=");

			String responseCode = str[0].substring(Res_Code + 1);
			String message = str[1].substring(Res_message + 1);
			System.out.println("responseCode=" + responseCode);
			System.out.println("message=" + message);
			System.out.println("chkValue=" + ChkValue);

			// 对收到的ChinaPay应答传回的域段进行验签
			boolean res = DigestMD5.MD5Verify(tiakong.getBytes("UTF-8"), ChkValue, PubKeyPath);

			TransactionBean charge = new TransactionBean();
			charge.setResponseCode(responseCode);
			charge.setMessage(message);
			charge.setPlain(tiakong);
			charge.setData(result);
			charge.setChkValue(ChkValue);

			request.setAttribute("chargeInput", charge);

			if (responseCode.equals("20FM")) {
				System.out.println("批量文件接口上传成功！");
			}
			if (res) {
				System.out.println("验签数据正确!");
				request.getRequestDispatcher("./Response.jsp").forward(request,
						response);
			} else {
				System.out.println("签名数据不匹配！");
				request.getRequestDispatcher("./VerifyFail.jsp").forward(
						request, response);
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			
			// 释放httpclient
			if (postMethod != null) {
				postMethod.releaseConnection();

			}
			if (null != httpClient) {
				SimpleHttpConnectionManager manager = (SimpleHttpConnectionManager) httpClient
						.getHttpConnectionManager();
				if (null == manager) {
					httpClient.getHttpConnectionManager().closeIdleConnections(
							0);
				} else {
					manager.shutdown();
				}
			}
			if (reader != null) {
				reader.close();
			}
			if (resInputStream != null) {
				resInputStream.close();
			}
		}
	}


}
