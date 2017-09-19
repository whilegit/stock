package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;

import com.jfinal.kit.StrKit;

import io.jpress.model.query.JBaseQuery;
import yjt.model.UnionpayLog;

public class UnionpayLogQuery extends JBaseQuery{
	protected static final UnionpayLog DAO = new UnionpayLog();
	private static final UnionpayLogQuery QUERY = new UnionpayLogQuery();
	
	public static UnionpayLogQuery me(){
		return QUERY;
	}
	
	public boolean isPaySnExists(String paySn) {
		return findByPaySn(paySn) != null;
	}
	
	public UnionpayLog findByPaySn(String paySn) {
		if(StrKit.isBlank(paySn)) return null;
		LinkedList<Object> params = new LinkedList<Object>();
		StringBuilder sqlBuilder = new StringBuilder();
		appendAndIfNotEmpty(sqlBuilder, "pay_sn", paySn, params);
		return DAO.doFindFirst(sqlBuilder.toString(), params.toArray());
	}
	
	public UnionpayLog findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


