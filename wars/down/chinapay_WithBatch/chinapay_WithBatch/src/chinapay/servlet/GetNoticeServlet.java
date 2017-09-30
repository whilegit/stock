package chinapay.servlet;

/**
 * 获取回盘文件生成通知
 * @author huang.xuting
 *
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chinapay.util.Config;
import chinapay.util.DigestMD5;

@SuppressWarnings("serial")
public class GetNoticeServlet extends HttpServlet {

	private static final String KEY_CHINAPAY_PUBKEY_FILEPATH = "chinapay.pubkey.filepath";

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		String PubKeyPath = null;

		Properties config = Config.getInstance().getProperties();
		PubKeyPath = config.getProperty(KEY_CHINAPAY_PUBKEY_FILEPATH);

		System.out.println("-----------------回盘通知开始-----------------");

		String responseCode = request.getParameter("responseCode");
		String message = request.getParameter("message");
		String merId = request.getParameter("merId");
		String orFileName = request.getParameter("orFileName");
		String fileName = request.getParameter("fileName");
		String chkValue = request.getParameter("chkValue");

		System.out.println("responseCode = " + responseCode);
		System.out.println("merId = " + merId);
		System.out.println("message = " + message);
		System.out.println("orFileName = " + orFileName);
		System.out.println("fileName = " + fileName);

		String plain = responseCode + message + merId + orFileName + fileName;
		System.out.println("验签明文：" + plain);

		try {
			
			System.out.println("-----验签开始-----");
			boolean res = DigestMD5.MD5Verify(plain.getBytes("UTF-8"), chkValue, PubKeyPath);
			System.out.println(res);
			if (res) {				
				System.out.println("验签正确！");
			} else {
				System.out.println("验签失败！");
			}

		} catch (Exception e) {
			System.out.println("未进行验签！");
		} finally {
			System.out.println("ChinapayOk");
			System.out.println("-----------------回盘通知结束-----------------");
			// 无论如何返回ChinapayOk
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write("ChinapayOk");
			out.flush();
		}
		return;
	}


}
