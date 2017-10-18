package yjt.model.query;

import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.User;
import io.jpress.model.query.JBaseQuery;
import io.jpress.utils.StringUtils;
import yjt.model.Apply;

public class ApplyQuery extends JBaseQuery{
	protected static final Apply DAO = new Apply();
	private static final ApplyQuery QUERY = new ApplyQuery();
	
	public static ApplyQuery me(){
		return QUERY;
	}
	
	public long findCount(ApplyQuery.Status stat) {
		StringBuilder sqlBuilder = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();
		if(stat != null){
			if(stat == ApplyQuery.Status.TIMEOUT || stat == ApplyQuery.Status.VALID) {
				String sign = (stat == ApplyQuery.Status.VALID) ? ">" : "<";
				sqlBuilder.append(" (`status` = ? And `create_time` "+sign+" ?)");
				params.add(Apply.Status.VALID.getIndex());
				long validTime = System.currentTimeMillis() - 86400L * 1000 * Apply.applyValidExpire;
				params.add( new Date(validTime));
			} else {
				sqlBuilder.append("status = ? ");
				params.add(stat.getIndex());
			}
		}

		if(params.isEmpty()){
			return DAO.doFindCount(sqlBuilder.toString());
		}else{
			return DAO.doFindCount(sqlBuilder.toString(), params.toArray());
		}
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
	
	public Page<Apply> paginateBySearch(int page, int pagesize, String keyword, String status) {
		return paginate(page, pagesize, keyword, status);
	}
	
	//注：此于的状态与Apply.Status略有不同，主要是增加了过期状态 = (VALID && 申请时间过期)
	public static enum Status{
		//WAIT状态表示：贷方无can_lend权限，其所达成的交易必须要经过后台核准。
		INVALID("无效", 0), VALID("等待交易", 1), DEALED("已达成合约", 2), WAIT("等待核准",3), TIMEOUT("已过期", 4);
		private String name;
		private int index;
	    private Status(String name, int index) {  
	        this.name = name;  
	        this.index = index;  
	    }
	    // 普通方法  
	    public static Status getEnum(int index){
	    	for (Status s : Status.values()) {  
	            if (s.getIndex() == index) {  
	                return s;  
	            }  
	        }
	        return null; 
	    }
	    
		public String getName() {	return name;	}
		public void setName(String name) {	this.name = name;	}
		public int getIndex() {	return index;	}
		public void setIndex(int index) {	this.index = index;	} 
	}
	
	public Page<Apply> paginate(int page, int pagesize, String keyword, String status) {

		String select = "select c.*";

		StringBuilder sql = new StringBuilder(" from apply c");
		sql.append(" left join user mc on c.apply_uid = mc.`id`");

		LinkedList<Object> params = new LinkedList<Object>();

		boolean needWhere = true;
		if(StrKit.notBlank(status) && StringUtils.isNumeric(status)) {
			ApplyQuery.Status s = ApplyQuery.Status.getEnum(Integer.parseInt(status));
			switch(s) {
				case INVALID:
				case DEALED:
				case WAIT:
					needWhere = appendIfNotEmpty(sql, "c.status", ""+s.getIndex(), params, needWhere);
					break;
				case VALID:
				case TIMEOUT:
					String sign = (s == ApplyQuery.Status.VALID) ? ">" : "<";
					
					needWhere = appendWhereOrAnd(sql, needWhere);
					sql.append(" (c.`status` = ? And c.`create_time` "+sign+" ?)");
					params.add(Apply.Status.VALID.getIndex());
					long validTime = System.currentTimeMillis() - 86400L * 1000 * Apply.applyValidExpire;
					params.add( new Date(validTime));
					
					break;
			}
		}

		if (StringUtils.isNotBlank(keyword)) {
			needWhere = appendWhereOrAnd(sql, needWhere);
			sql.append(" (mc.`realname` like ?)");
			params.add(keyword);
		}
		
		sql.append(" group by c.id");
		sql.append(" ORDER BY c.id DESC");

		if (params.isEmpty()) {
			return DAO.paginate(page, pagesize, true, select, sql.toString());
		}

		System.out.println(JSON.toJSONString(params));
		return DAO.paginate(page, pagesize, true, select, sql.toString(), params.toArray());
	}
	
	public Apply findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


