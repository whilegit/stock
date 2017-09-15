package yjt.model;

import java.util.Date;

import io.jpress.model.core.Table;
import yjt.Utils;
import yjt.model.base.BaseUnionpayLog;
import yjt.model.query.UnionpayLogQuery;

@Table(tableName = "unionpaylog", primaryKey = "id")
public class UnionpayLog extends BaseUnionpayLog<UnionpayLog>{

	private static final long serialVersionUID = 1L;
	
	protected static String genUniquePaySn_phy(Date now) {
		String ymdhms = Utils.getDayNumber(now);
		return "PA" + ymdhms + String.format("%06d", Utils.random(1, 999999));
	}
	
	public static String genUniquePaySn(Date now) {
		String paySnTmp = "";
		String paySn = null;
		for(int i = 0; i<10; i++) {
			paySnTmp = genUniquePaySn_phy(now);
			if(UnionpayLogQuery.me().isPaySnExists(paySnTmp) == false) {
				paySn = paySnTmp;
				break;
			}
		}
		return paySn;
	}
}

