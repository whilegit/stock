package yjt.model.query;

import java.math.BigInteger;

import io.jpress.model.query.JBaseQuery;
import yjt.model.Apply;
import yjt.model.Contract;

public class ApplyQuery extends JBaseQuery{
	protected static final Apply DAO = new Apply();
	private static final ApplyQuery QUERY = new ApplyQuery();
	
	public static ApplyQuery me(){
		return QUERY;
	}
	
	public Apply findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


