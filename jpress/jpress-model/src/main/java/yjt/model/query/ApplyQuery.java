package yjt.model.query;

import io.jpress.model.query.JBaseQuery;
import yjt.model.Apply;

public class ApplyQuery extends JBaseQuery{
	protected static final Apply DAO = new Apply();
	private static final ApplyQuery QUERY = new ApplyQuery();
	
	public static ApplyQuery me(){
		return QUERY;
	}
}


