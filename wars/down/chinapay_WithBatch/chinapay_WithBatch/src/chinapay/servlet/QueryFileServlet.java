package chinapay.servlet;

/**
 * 批量代扣文件查询
 * @author huang.xuting
 *
 */

import java.io.BufferedReader;
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

@SuppressWarnings("serial")
public class QueryFileServlet extends HttpServlet {

	public int tmpResultLength = -1;
	private static final String KEY_CHINAPAY_MERID = "chinapay.merid";
	private static final String KEY_CHINAPAY_MERKEY_FILEPATH = "chinapay.merkey.filepath";
	private static final String KEY_CHINAPAY_PUBKEY_FILEPATH = "chinapay.pubkey.filepath";
	private static final String PaymentUrl = "chinapay.QueryFile.url";

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

		request.setCharacterEncoding("UTF-8");
		String TransDate = request.getParameter("TransDate");
		String MerSeqId = request.getParameter("MerSeqId");

		// 原始文件命名规范：MERID_YYYYMMDD_XXXXXX_Q.TXT
		String fileName = merId + "_" + TransDate + "_" + MerSeqId + "_Q.txt";
		System.out.println("fileName=[" + fileName + "]");

		// 签名明文
		String signMsg = merId + fileName;

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

			// 对需要上传的字段签名
			String chkValue2 = null;
			chkValue2 = DigestMD5.MD5Sign(merId, signMsg.getBytes("UTF-8"), MerKeyPath);
			System.out.println("批量代扣文件查询接口签名内容:" + chkValue2);

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
			reader = new BufferedReader(new InputStreamReader(resInputStream,
					"UTF-8"));
			String tempBf = null;
			StringBuffer html = new StringBuffer();
			while ((tempBf = reader.readLine()) != null) {

				html.append(tempBf);
			}

			String result = html.toString();
			System.out.println("批量代扣文件查询接口返回报文result=[" + result + "]");

			// 拆分页面应答数据
			int dex = result.lastIndexOf("=");
			String tiakong = result.substring(0, dex + 1);
			String ChkValue = result.substring(dex + 1);

			String str[] = result.split("&");
			System.out.println(str.length);
			TransactionBean charge = new TransactionBean();
			// 验签明文
			String plainData = "";

			// 回盘文件下载失败
			System.out.println("验签明文：");
			plainData = tiakong;
			System.out.println(plainData);
			int Res = str[0].indexOf("=");
			String ResponseCode = str[0].substring(Res + 1);
			String Message = str[1].substring(str[1].indexOf("=") + 1);
			System.out.println("responseCode = " + ResponseCode);
			charge.setResponseCode(ResponseCode);
			charge.setMessage(Message);
			charge.setPlain(tiakong);
			charge.setData(result);

			// 对收到的ChinaPay应答传回的域段进行验签
			boolean res = DigestMD5.MD5Verify(plainData.getBytes("UTF-8"), ChkValue, PubKeyPath);
			request.setAttribute("chargeInput", charge);

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
