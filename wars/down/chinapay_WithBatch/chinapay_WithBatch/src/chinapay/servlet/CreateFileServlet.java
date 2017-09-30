package chinapay.servlet;

/**
 * 生成批量交易信息
 * @author huang.xuting
 *
 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chinapay.bean.TransactionBean;
import chinapay.util.Config;

public class CreateFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String KEY_CHINAPAY_MERID = "chinapay.merid";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String MerId = null;
		Properties config = Config.getInstance().getProperties();
		MerId = config.getProperty(KEY_CHINAPAY_MERID);

		request.setCharacterEncoding("UTF-8");
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sf2 = new SimpleDateFormat("HHmmss");
		Date dt = new Date();

		String MerSeqId = sf2.format(dt); // 批次号
		String TransDate = sf1.format(dt); // 交易日期

		int n = 5; // 批量交易数量

		// 文件的第一行，包含：商户号，批次号，总笔数，总金额，各项用“|”分割
		String plain = MerId + "|" + MerSeqId + "|5|5000";
		System.out.println("文件头：" + plain);

		/*
		 * 一行代表一笔交易记录。在每一行交易记录中，交易所需的各项都用“|”隔开
		 * 商户日期，流水号，开户行代号，卡折标志，卡号/折号，持卡人姓名，证件类型，证件号码，金额，用途，私有域
		 */
		for (int i = 1; i <= n; i++) {
			String OrdId = TransDate + MerSeqId + "0" + i;
			plain += "\r\n"
					+ TransDate
					+ "|"
					+ OrdId
					+ "|0103|0|6228401111111111111|持卡人姓名|01|310104200005053209|1000|用途|私有域";

		}

		System.out.println("文件内容：");
		System.out.println(plain);

		// 文件命名规范：MERID_YYYYMMDD_XXXXXX_Q.TXT
		String fileName = MerId + "_" + TransDate + "_" + MerSeqId + "_Q.txt";
		System.out.println("fileName=[" + fileName + "]");

		TransactionBean charge = new TransactionBean();
		charge.setPlain(plain);
		charge.setData(fileName);
		request.setAttribute("chargeInput", charge);

		request.getRequestDispatcher("./WithBatch.jsp").forward(request,
				response);
	}
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}


	
}
