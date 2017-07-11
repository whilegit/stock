
package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.jfinal.kit.StrKit;
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
	
	public List<Contract> findList(int page, int pageSize, Contract.Status cs, String contractNumber, BigInteger debitUid, BigInteger creditUid){
		StringBuilder sqlBuilder = new StringBuilder("Select * From contract as c ");
		LinkedList<Object> params = new LinkedList<Object>();
		
		boolean needWhere = true;
		if(cs != null && cs != Contract.Status.ALL)
			needWhere = appendIfNotEmpty(sqlBuilder, "c.status", String.valueOf(cs.getIndex()), params, needWhere);
		if(StrKit.notBlank(contractNumber)){
			needWhere = appendIfNotEmpty(sqlBuilder, "c.contract_number", contractNumber, params, needWhere);
		}
		String debitId = (debitUid != null) ? debitUid.toString() : null;
		needWhere = appendIfNotEmpty(sqlBuilder, "c.debit_id", debitId, params, needWhere);
		
		String creditId = (creditUid != null) ? creditUid.toString() : null;
		needWhere = appendIfNotEmpty(sqlBuilder, "c.credit_id", creditId, params, needWhere);
		

		sqlBuilder.append(" Order By c.id Desc Limit ?, ?");
		params.add((page -1)*pageSize);
		params.add(pageSize);
		if(params.isEmpty()){
			return DAO.find(sqlBuilder.toString());
		}else{
			return DAO.find(sqlBuilder.toString(), params.toArray());
		}
	}
	
	public long findCount(Contract.Status cs, String contractNumber, BigInteger debitUid, BigInteger creditUid) {
		StringBuilder sqlBuilder = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();
		
		if(cs != null && cs != Contract.Status.ALL)
			appendAndIfNotEmpty(sqlBuilder, "status", String.valueOf(cs.getIndex()), params);
		if(StrKit.notBlank(contractNumber))
			appendAndIfNotEmpty(sqlBuilder, "contract_number", contractNumber, params);
		
		String debitId = (debitUid != null) ? debitUid.toString() : null;
		appendAndIfNotEmpty(sqlBuilder, "debit_id", debitId, params);
		
		String creditId = (creditUid != null) ? creditUid.toString() : null;
		appendAndIfNotEmpty(sqlBuilder, "credit_id", creditId, params);

		if(params.isEmpty()){
			return DAO.doFindCount(sqlBuilder.toString());
		}else{
			return DAO.doFindCount(sqlBuilder.toString(), params.toArray());
		}
	}
}
