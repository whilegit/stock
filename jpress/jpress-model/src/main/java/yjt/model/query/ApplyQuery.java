package yjt.model.query;

import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.jpress.model.User;
import io.jpress.model.query.JBaseQuery;
import yjt.model.Apply;

public class ApplyQuery extends JBaseQuery{
	protected static final Apply DAO = new Apply();
	private static final ApplyQuery QUERY = new ApplyQuery();
	
	public static ApplyQuery me(){
		return QUERY;
	}
	
	public List<Apply> findList(Integer page, Integer pageSize, Apply.Status[] stats, BigInteger applyUid, BigInteger friendId, int daysBefore){
		StringBuilder sqlBuilder = new StringBuilder("Select * From apply ");
		LinkedList<Object> params = new LinkedList<Object>();
		
		boolean needWhere = true;
		if(stats != null && stats.length > 0) {
			if(stats.length == 1){
				sqlBuilder.append("Where status = ? ");
				params.add(stats[0].getIndex());
			} else {
				sqlBuilder.append("Where status in (");
				for(int i = 0; i<stats.length; i++){
					if(i != 0) sqlBuilder.append(",");
					sqlBuilder.append("?");
					params.add(stats[i].getIndex());
				}
				sqlBuilder.append(") ");
			}
			needWhere = false;
		}
		
		String applyUidStr = (applyUid != null) ? applyUid.toString() : null;
		needWhere = appendIfNotEmpty(sqlBuilder, "apply_uid", applyUidStr, params, needWhere);
		if(friendId != null){
			if(needWhere) sqlBuilder.append("Where ");
			else sqlBuilder.append("And ");
			sqlBuilder.append("FIND_IN_SET(?,to_friends) > 0 ");
			params.add(friendId.toString());
			needWhere = false;
		}
		
		if(needWhere) sqlBuilder.append("Where ");
		else sqlBuilder.append("And ");
		sqlBuilder.append("create_time > ? " );
		params.add(new Date(System.currentTimeMillis() - 86400L * 1000 * Apply.applyValidExpire));
		needWhere = false;
		
		if(page != null && pageSize != null){
			sqlBuilder.append(" Order By id Desc Limit ?, ?");
			params.add((page -1)*pageSize);
			params.add(pageSize);
		}
		if(params.isEmpty()){
			return DAO.find(sqlBuilder.toString());
		}else{
			return DAO.find(sqlBuilder.toString(), params.toArray());
		}
	}
	
	public boolean checkExceedCredit(User user, double newBorrow) {
		if(user == null) return true;
		double usedCredit = 0.0;
		//查找未达成交易且未过期的申请
		Apply.Status[] stats = {Apply.Status.VALID};
		List<Apply> unTradedApplies = findList(null, null, stats, user.getId(), null, Apply.applyValidExpire);
		if(unTradedApplies != null && unTradedApplies.size() > 0) {
			for(Apply apply : unTradedApplies) {
				usedCredit += apply.getAmount().doubleValue();
			}
		}
		//查找合约中未还款的交易
		usedCredit += ContractQuery.me().queryCurDebits(user.getId());
		int canBorrowMoney = user.getCanBorrowMoney();
		return (double)canBorrowMoney < usedCredit + newBorrow;
	}
	
	public Apply findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


