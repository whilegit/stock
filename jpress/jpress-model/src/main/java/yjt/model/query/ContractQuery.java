
package yjt.model.query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.jfinal.kit.StrKit;

import io.jpress.model.core.Jdb;
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
	
	/**
	 * 查询借入或贷出的总金额，仅统计正处于还款期或展期的金额
	 * @param debitUid　
	 * @param creditUid
	 * @return
	 */
	protected double queryAmount(BigInteger debitUid, BigInteger creditUid){
		int establish = Contract.Status.ESTABLISH.getIndex();
		int extend = Contract.Status.EXTEND.getIndex();
		//int lost = Contract.Status.LOST.getIndex();
		LinkedList<Object> params = new LinkedList<Object>();
		StringBuilder sqlBuilder = new StringBuilder("Select sum(amount) as money From contract Where status in(?,?)");
		params.add(establish);
		params.add(extend);
		//params.add(lost);
		if(debitUid!=null) appendAndIfNotEmpty(sqlBuilder, "debit_id", debitUid.toString(), params);
		if(creditUid!=null) appendAndIfNotEmpty(sqlBuilder, "credit_id", creditUid.toString(), params);
		BigDecimal ret = Jdb.queryBigDecimal(sqlBuilder.toString(), params.toArray());
		return ret == null ? 0.00 : ret.doubleValue();
	}
	
	/**
	 * 查询借入总金额
	 * @param debitUid
	 * @return
	 */
	public double queryDebits(BigInteger debitUid){
		return queryAmount(debitUid, null);
	}
	
	/**
	 * 查询贷出总金额
	 * @param creditUid
	 * @return
	 */
	public double queryCredits(BigInteger creditUid){
		return queryAmount(null, creditUid);
	}
	
	
	public Contract findById(final BigInteger id) {
		return DAO.findById(id);
	}
}
