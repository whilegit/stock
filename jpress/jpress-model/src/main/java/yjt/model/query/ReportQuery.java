package yjt.model.query;

import java.math.BigInteger;

import io.jpress.model.query.JBaseQuery;
import yjt.model.Apply;

public class ReportQuery extends JBaseQuery{
	protected static final Apply DAO = new Apply();
	private static final ReportQuery QUERY = new ReportQuery();
	
	public static ReportQuery me(){
		return QUERY;
	}
	
	public Apply findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


