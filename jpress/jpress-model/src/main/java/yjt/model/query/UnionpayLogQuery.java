package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;

import io.jpress.model.query.JBaseQuery;
import yjt.model.UnionpayLog;

public class UnionpayLogQuery extends JBaseQuery{
	protected static final UnionpayLog DAO = new UnionpayLog();
	private static final UnionpayLogQuery QUERY = new UnionpayLogQuery();
	
	public static UnionpayLogQuery me(){
		return QUERY;
	}
	
	public boolean isPaySnExists(String paySn) {
		LinkedList<Object> params = new LinkedList<Object>();
		StringBuilder sqlBuilder = new StringBuilder("Select id From unionpaylog Where pay_sn = ? Order By id Desc Limit 1");
		params.add(paySn);
		UnionpayLog paylog = DAO.findFirst(sqlBuilder.toString(), params);
		return paylog != null;
	}
	
	public UnionpayLog findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


