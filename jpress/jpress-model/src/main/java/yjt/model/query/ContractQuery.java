
package yjt.model.query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.Content;
import io.jpress.model.User;
import io.jpress.model.core.Jdb;
import io.jpress.model.query.JBaseQuery;
import io.jpress.model.query.UserQuery;
import io.jpress.utils.StringUtils;
import yjt.Utils;
import yjt.model.Contract;
import yjt.model.Contract.Status;

/**
 * @author lzr
 */
public class ContractQuery extends JBaseQuery{
	protected static final Contract DAO = new Contract();
	private static final ContractQuery QUERY = new ContractQuery();
	
	public static ContractQuery me(){
		return QUERY;
	}
	
	public List<Contract> findList(Integer page, Integer pageSize, Contract.Status cs, String contractNumber, BigInteger debitUid, BigInteger creditUid){
		Contract.Status[] stats = new Status[0];
		if(cs != null && cs != Status.ALL) {
			stats = new Status[] { cs };
		}
		return findList(page, pageSize, stats, contractNumber, debitUid, creditUid);
	}
	
	public List<Contract> findList(Integer page, Integer pageSize, Contract.Status[] stats, String contractNumber, BigInteger debitUid, BigInteger creditUid){
		StringBuilder sqlBuilder = new StringBuilder("Select * From contract as c ");
		LinkedList<Object> params = new LinkedList<Object>();
		
		boolean needWhere = true;
		if(stats != null && stats.length > 0) {
			if(stats.length == 1){
				sqlBuilder.append("Where c.status = ? ");
				params.add(stats[0].getIndex());
			} else {
				sqlBuilder.append("Where c.status in (");
				for(int i = 0; i<stats.length; i++){
					if(i != 0) sqlBuilder.append(",");
					sqlBuilder.append("?");
					params.add(stats[i].getIndex());
				}
				sqlBuilder.append(") ");
			}
			needWhere = false;
		}
		
		if(StrKit.notBlank(contractNumber)){
			needWhere = appendIfNotEmpty(sqlBuilder, "c.contract_number", contractNumber, params, needWhere);
		}
		String debitId = (debitUid != null) ? debitUid.toString() : null;
		needWhere = appendIfNotEmpty(sqlBuilder, "c.debit_id", debitId, params, needWhere);
		
		String creditId = (creditUid != null) ? creditUid.toString() : null;
		needWhere = appendIfNotEmpty(sqlBuilder, "c.credit_id", creditId, params, needWhere);
		
		if(page != null && pageSize != null){
			sqlBuilder.append(" Order By c.id Desc Limit ?, ?");
			params.add((page -1)*pageSize);
			params.add(pageSize);
		}
		if(params.isEmpty()){
			return DAO.find(sqlBuilder.toString());
		}else{
			return DAO.find(sqlBuilder.toString(), params.toArray());
		}
	}
	
	public Page<Contract> paginateBySearch(int page, int pagesize, String keyword, String status) {
		return paginate(page, pagesize, keyword, status);
	}
	
	public Page<Contract> paginate(int page, int pagesize, String keyword, String status) {

		String select = "select c.*";

		StringBuilder sql = new StringBuilder(" from contract c");
		sql.append(" left join user mc on c.credit_id = mc.`id`");
		sql.append(" left join user md on c.`debit_id` = md.`id`");

		LinkedList<Object> params = new LinkedList<Object>();

		boolean needWhere = true;
		if(StrKit.notBlank(status))
			needWhere = appendIfNotEmpty(sql, "c.status", status, params, needWhere);
		
		if (StringUtils.isNotBlank(keyword)) {
			needWhere = appendWhereOrAnd(sql, needWhere);
			sql.append(" (c.`contract_number` like ? Or mc.`realname` like ? Or md.`realname` like ?)");
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
		}
		
		sql.append(" group by c.id");
		sql.append(" ORDER BY c.id DESC");

		if (params.isEmpty()) {
			return DAO.paginate(page, pagesize, true, select, sql.toString());
		}

		System.out.println(JSON.toJSONString(params));
		return DAO.paginate(page, pagesize, true, select, sql.toString(), params.toArray());
	}
	
	public List<Contract> queryContracts(BigInteger debitor, BigInteger creditor, Contract.Status[] stats){
		if(stats == null) 
			stats = new Status[]{Status.ESTABLISH, Status.EXTEND, Status.FINISH, Status.LOST};
		
		StringBuilder sqlBuilder = new StringBuilder("Select * From contract Where ");
		LinkedList<Object> params = new LinkedList<Object>();
		
		if(stats != null){
			if(stats.length == 1){
				sqlBuilder.append("status = ? ");
				params.add(stats[0].getIndex());
			} else {
				sqlBuilder.append("status in (");
				for(int i = 0; i<stats.length; i++){
					if(i != 0) sqlBuilder.append(",");
					sqlBuilder.append("?");
					params.add(stats[i].getIndex());
				}
				sqlBuilder.append(") ");
			}
		}
		if(debitor != null) {
			sqlBuilder.append(" And debit_id = ? ");
			params.add(debitor.toString());
		}
		if(creditor != null) {
			sqlBuilder.append(" And debit_id = ? ");
			params.add(creditor.toString());
		}
		
		return DAO.find(sqlBuilder.toString(), params.toArray());
	}
	
	public long findCount(Contract.Status[] stats, String contractNumber, BigInteger debitUid, BigInteger creditUid) {
		StringBuilder sqlBuilder = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();
		
		if(stats != null){
			if(stats.length == 1){
				sqlBuilder.append("status = ? ");
				params.add(stats[0].getIndex());
			} else {
				sqlBuilder.append("status in (");
				for(int i = 0; i<stats.length; i++){
					if(i != 0) sqlBuilder.append(",");
					sqlBuilder.append("?");
					params.add(stats[i].getIndex());
				}
				sqlBuilder.append(") ");
			}
		}
		
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
	
	public long findCount(Contract.Status stat, String contractNumber, BigInteger debitUid, BigInteger creditUid) {
		Contract.Status[] stats;
		if(stat == null || stat == Contract.Status.ALL) {
			stats = new Contract.Status[] {Status.ESTABLISH, Status.EXTEND, Status.FINISH, Status.LOST};
		} else {
			stats = new Contract.Status[] {stat};
		}
		return findCount(stats, contractNumber, debitUid, creditUid);
	}
	
	
	public boolean isContractNumberExists(String contractNumber) {
		LinkedList<Object> params = new LinkedList<Object>();
		StringBuilder sqlBuilder = new StringBuilder("Select id From contract Where contract_number = ? Order By id Desc Limit 1");
		params.add(contractNumber);
		Contract contract = DAO.findFirst(sqlBuilder.toString(), params);
		return contract != null;
	}
	
	public Contract findByApplyId(final BigInteger applyId){
		LinkedList<Object> params = new LinkedList<Object>();
		StringBuilder sqlBuilder = new StringBuilder("Select id From contract Where apply_id = ? And status>=5 Order By id Desc Limit 1");
		params.add(applyId.toString());
		Contract contract = DAO.findFirst(sqlBuilder.toString(), params);
		return contract;
	}
	
	/**
	 * 查询借入或贷出的总金额，仅统计正处于还款期或展期的金额
	 * @param debitUid　
	 * @param creditUid
	 * @return
	 */
	protected double queryAmount(BigInteger debitUid, BigInteger creditUid, Contract.Status[] stats){
		LinkedList<Object> params = new LinkedList<Object>();
		StringBuilder sqlBuilder = new StringBuilder("Select sum(amount) as money From contract Where ");
		if(stats.length == 1){
			sqlBuilder.append("status = ? ");
			params.add(stats[0].getIndex());
		} else {
			sqlBuilder.append("status in (");
			for(int i = 0; i<stats.length; i++){
				if(i != 0) sqlBuilder.append(",");
				sqlBuilder.append("?");
				params.add(stats[i].getIndex());
			}
			sqlBuilder.append(") ");
		}
		
		//params.add(lost);
		if(debitUid!=null) appendAndIfNotEmpty(sqlBuilder, "debit_id", debitUid.toString(), params);
		if(creditUid!=null) appendAndIfNotEmpty(sqlBuilder, "credit_id", creditUid.toString(), params);
		BigDecimal ret = Jdb.queryBigDecimal(sqlBuilder.toString(), params.toArray());
		return ret == null ? 0.00 : ret.doubleValue();
	}
	
	/**
	 * 查询当前借入总金额
	 * @param debitUid
	 * @return
	 */
	public double queryCurDebits(BigInteger debitUid){
		Contract.Status[] stats = {Status.ESTABLISH, Status.EXTEND};
		return queryAmount(debitUid, null, stats);
	}
	
	/**
	 * 查询所有借款总金额
	 * @param debitUid
	 * @return
	 */
	public double queryTotalDebits(BigInteger debitUid){
		Contract.Status[] stats = {Status.ESTABLISH, Status.EXTEND, Status.FINISH, Status.LOST};
		return queryAmount(debitUid, null, stats);
	}
	
	/**
	 * 查询当前贷出总金额
	 * @param creditUid
	 * @return
	 */
	public double queryCurCredits(BigInteger creditUid){
		Contract.Status[] stats = {Status.ESTABLISH, Status.EXTEND};
		return queryAmount(null, creditUid, stats);
	}
	
	/**
	 * 查询所有贷出总金额
	 * @param creditUid
	 * @return
	 */
	public double queryTotalCredits(BigInteger creditUid){
		Contract.Status[] stats = {Status.ESTABLISH, Status.EXTEND, Status.FINISH, Status.LOST};
		return queryAmount(null, creditUid, stats);
	}
	


	
	public long queryDiffOppositors(BigInteger uid){
		List<Contract> list = queryContracts(uid, null, null);
		Set<BigInteger> set = new HashSet<BigInteger>();
		for(Contract contract : list){
			BigInteger debitor = contract.getDebitId();
			BigInteger creditor = contract.getCreditId();
			if(debitor.equals(uid) == false){
				set.add(debitor);
			} else {
				set.add(creditor);
			}
		}
		return set.size();
	}
	
	public JSONObject contractBetween(BigInteger debitor, BigInteger creditor){
		JSONObject json = new JSONObject();
		Contract.Status[] totalStats = {Status.ESTABLISH, Status.EXTEND, Status.FINISH, Status.LOST};
		Contract.Status[] curStats = {Status.ESTABLISH, Status.EXTEND};
		long totalCount = findCount(totalStats, null, debitor, creditor);
		long curCount = findCount(curStats, null, debitor, creditor);
		double curAmount = queryAmount(debitor, creditor, curStats);
		json.put("usCrtMoneyAmount", String.format("%.2f", curAmount) );      //当前debitor向creditor借入的交易总金额
		json.put("usCrtMoneyCount", "" + curCount);        //当前debitor向creditor借入的交易笔数
		json.put("usCrtAllMoneyCount", "" + totalCount);   //debitort向creditor借入的累计交易笔数
		return json;
	}
	
	public JSONObject contractStatics(BigInteger uid){
		Contract.Status[] totalStats = {Status.ESTABLISH, Status.EXTEND, Status.FINISH, Status.LOST};
		Contract.Status[] curStats = {Status.ESTABLISH, Status.EXTEND};
		Contract.Status[] finishedStat = {Status.FINISH};
		
		JSONObject json = new JSONObject();
		double userMoneyAmount = ContractQuery.me().queryCurDebits(uid);
		long userMoneyCount = findCount(curStats, null, uid, null);
		long userAllMoneyCount = findCount(totalStats, null, uid, null);
		double returnMoneyAmount = queryAmount(uid, null, finishedStat);
		long dealFriendCount = queryDiffOppositors(uid);
		json.put("userMoneyAmount", String.format("%.2f", userMoneyAmount));      //当前借入总金额
		json.put("userMoneyCount", ""+userMoneyCount);        //当前借入笔数
		json.put("userAllMoneyCount", ""+userAllMoneyCount);  //累计借入笔数
		json.put("returnMoneyAmount", String.format("%.2f", returnMoneyAmount));  //累计已偿还总金额
		json.put("dealFriendCount", ""+dealFriendCount);      //交易的好友数
		return json;
	}
	
	
	public JSONObject inOutList(BigInteger uid, String type, int page, int pageSize) {
		JSONObject  ret = new JSONObject();
		Contract.Status[] curStats = {Status.ESTABLISH, Status.EXTEND};
		List<Contract> list = null;
		long totalItems = 0;
		if(type.equals("in")) {
			list = findList(page, pageSize, curStats, null, uid, null);
			totalItems = findCount(curStats, null, uid, null);
		} else {
			list = findList(page, pageSize, curStats, null, null, uid);
			totalItems = findCount(curStats, null, null, uid);
		}
		List<JSONObject>  result = new ArrayList<JSONObject>();
		for(Contract contract : list) {
			JSONObject js = new JSONObject();
			js.put("id", contract.getId().toString());
			js.put("sn", contract.getContractNumber());
			js.put("toUserID", contract.getDebitId().toString());
			js.put("toUser", contract.getDebitUser().getRealname());
			js.put("fromUserID", contract.getCreditId().toString());
			js.put("fromUser", contract.getCreditUser().getRealname());
			js.put("money", Utils.bigDecimalRound2(contract.getAmount()));
			js.put("rate", Utils.bigDecimalRound2(contract.getAnnualRate()));
			String interest = "-";
			if(Contract.RepaymentMethod.getEnum(contract.getRepaymentMethod()) == Contract.RepaymentMethod.WHOLE_AT_MATURITY) {
				//到期还本付息的还款方式才需要计算利息
				interest = String.format("%.2f", contract.calcInterest());
			} else {
				//其它类型的还款方式需要重新设计接口
			}
			js.put("interest", interest);
			js.put("startDate", Utils.toYmd(contract.getValueDate()));
			js.put("endDate", Utils.toYmd(contract.getMaturityDate()));
			js.put("day", ""+Utils.days(new Date(), contract.getMaturityDate()));
			result.add(js);
		}
		ret.put("page", "" + page);
		ret.put("pageSize", "" + pageSize);
		ret.put("totalItems", "" + totalItems);
		ret.put("result", result);
		return ret;
	}
	
	public Contract findById(final BigInteger id) {
		return DAO.findById(id);
	}
}
