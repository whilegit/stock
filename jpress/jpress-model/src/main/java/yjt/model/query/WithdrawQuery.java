package yjt.model.query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.core.Jdb;
import io.jpress.model.query.JBaseQuery;
import io.jpress.utils.StringUtils;
import yjt.Utils;
import yjt.model.Withdraw;

public class WithdrawQuery extends JBaseQuery{
	protected static final Withdraw DAO = new Withdraw();
	private static final WithdrawQuery QUERY = new WithdrawQuery();
	
	public static WithdrawQuery me(){
		return QUERY;
	}
	
	public long findCount(Withdraw.Status stat) {
		StringBuilder sqlBuilder = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();
		if(stat != null){
			sqlBuilder.append("status = ? ");
			params.add(stat.getIndex());
		}

		if(params.isEmpty()){
			return DAO.doFindCount(sqlBuilder.toString());
		}else{
			return DAO.doFindCount(sqlBuilder.toString(), params.toArray());
		}
	}
	
	public long findCountToday() {
		StringBuilder sqlBuilder = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();

		sqlBuilder.append("create_time > ? ");
		params.add(Utils.toYmdHms(new Date(Utils.getDayStartTime(new Date()))));

		if(params.isEmpty()){
			return DAO.doFindCount(sqlBuilder.toString());
		}else{
			return DAO.doFindCount(sqlBuilder.toString(), params.toArray());
		}
	}
	
	public Page<Withdraw> paginateBySearch(int page, int pagesize, String keyword, String status, Long fromTime) {
		return paginate(page, pagesize, keyword, status, fromTime);
	}
	
	public Page<Withdraw> paginate(int page, int pagesize, String keyword, String status, Long fromTime) {

		String select = "select c.*";

		StringBuilder sql = new StringBuilder(" from withdraw c");
		sql.append(" left join user mc on c.user_id = mc.`id`");

		LinkedList<Object> params = new LinkedList<Object>();

		boolean needWhere = true;
		if(StrKit.notBlank(status) && StringUtils.isNumeric(status)) {
			Withdraw.Status s = Withdraw.Status.getEnum(Integer.parseInt(status));
			needWhere = appendIfNotEmpty(sql, "c.status", ""+s.getIndex(), params, needWhere);
		}

		if (StringUtils.isNotBlank(keyword)) {
			needWhere = appendWhereOrAnd(sql, needWhere);
			sql.append(" (mc.`realname` like ?)");
			params.add(keyword);
		}
		
		if(fromTime != null) {
			needWhere = appendWhereOrAnd(sql, needWhere);
			sql.append("c.create_time>=?");
			params.add(Utils.toYmdHms(new Date(fromTime)));
		}
		
		sql.append(" group by c.id");
		sql.append(" ORDER BY c.id DESC");

		if (params.isEmpty()) {
			return DAO.paginate(page, pagesize, true, select, sql.toString());
		}

		return DAO.paginate(page, pagesize, true, select, sql.toString(), params.toArray());
	}
	
	public double queryAmount(Integer status, Long fromTime, Long toTime){
		LinkedList<Object> params = new LinkedList<Object>();
		StringBuilder sqlBuilder = new StringBuilder("Select sum(money) as totalMoney From withdraw Where 1 ");
		if(fromTime != null) {
			sqlBuilder.append(" and create_time >= ? ");
			params.add(Utils.toYmdHms(new Date(fromTime)));
		}
		if(toTime != null) {
			sqlBuilder.append(" and create_time < ? ");
			params.add(Utils.toYmdHms(new Date(toTime)));
		}
		if(status != null) {
			sqlBuilder.append(" and status = ? ");
			params.add(status);
		}
		
		BigDecimal ret = Jdb.queryBigDecimal(sqlBuilder.toString(), params.toArray());
		return ret == null ? 0.00 : ret.doubleValue();
	}
	
	public Withdraw findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


