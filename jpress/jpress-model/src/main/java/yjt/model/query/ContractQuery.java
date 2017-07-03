
package yjt.model.query;

import java.util.LinkedList;
import java.util.List;

import io.jpress.model.query.JBaseQuery;
import yjt.model.Contract;

/**
 * @author lzr
 */
public class ContractQuery extends JBaseQuery{
	protected static final Contract DAO = new Contract();
	private static final ContractQuery QUERY = new ContractQuery();
	
	public static ContractQuery me(){
		return QUERY;
	}
	
	public List<Contract> findList(int page, int pageSize, String status){
		StringBuilder sqlBuilder = new StringBuilder("Select * From contract as c ");
		LinkedList<Object> params = new LinkedList<Object>();
		
		boolean needWhere = true;
		needWhere = appendIfNotEmpty(sqlBuilder, "c.status", status, params, needWhere);
		
		sqlBuilder.append(" Order By c.id Desc Limit ?, ?");
		params.add(page -1);
		params.add(pageSize);
		if(params.isEmpty()){
			return DAO.find(sqlBuilder.toString());
		}else{
			return DAO.find(sqlBuilder.toString(), params.toArray());
		}
	}
}
