package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import io.jpress.model.query.JBaseQuery;
import yjt.model.Ad;

public class AdQuery extends JBaseQuery{
	protected static final Ad DAO = new Ad();
	private static final AdQuery QUERY = new AdQuery();
	
	public static AdQuery me(){
		return QUERY;
	}
	
	public List<Ad> findList(){
		StringBuilder sqlBuilder = new StringBuilder("Select * From ad ");
		LinkedList<Object> params = new LinkedList<Object>();
		sqlBuilder.append(" Order By order_num Desc");

		if(params.isEmpty()){
			return DAO.find(sqlBuilder.toString());
		}else{
			return DAO.find(sqlBuilder.toString(), params.toArray());
		}
	}
	
	public Ad findById(final BigInteger id) {
		return DAO.findById(id);
	}
	
	
}


