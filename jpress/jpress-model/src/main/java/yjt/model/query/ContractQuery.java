
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
	
	public List<Contract> findList(int page, int pageSize, Contract.Status cs){
		StringBuilder sqlBuilder = new StringBuilder("Select * From contract as c ");
		LinkedList<Object> params = new LinkedList<Object>();
		
		boolean needWhere = true;
		if(cs != null)
			needWhere = appendIfNotEmpty(sqlBuilder, "c.status", String.valueOf(cs.getIndex()), params, needWhere);
		
		sqlBuilder.append(" Order By c.id Desc Limit ?, ?");
		params.add((page -1)*pageSize);
		params.add(pageSize);
		if(params.isEmpty()){
			return DAO.find(sqlBuilder.toString());
		}else{
			return DAO.find(sqlBuilder.toString(), params.toArray());
		}
	}
	
	public long findCountByStatus(Contract.Status cs) {
		if(cs == Contract.Status.ALL) return DAO.doFindCount();
		else return DAO.doFindCount("status = ?", cs.getIndex());
	}
}
