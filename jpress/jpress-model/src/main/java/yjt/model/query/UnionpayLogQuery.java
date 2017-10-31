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
import yjt.model.Contract;
import yjt.model.UnionpayLog;

public class UnionpayLogQuery extends JBaseQuery{
	protected static final UnionpayLog DAO = new UnionpayLog();
	private static final UnionpayLogQuery QUERY = new UnionpayLogQuery();
	
	public static UnionpayLogQuery me(){
		return QUERY;
	}
	
	public boolean isPaySnExists(String paySn) {
		return findByPaySn(paySn) != null;
	}
	
	public long findCount() {
		StringBuilder sqlBuilder = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();

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
		sqlBuilder.append("and status=1");
		
		if(params.isEmpty()){
			return DAO.doFindCount(sqlBuilder.toString());
		}else{
			return DAO.doFindCount(sqlBuilder.toString(), params.toArray());
		}
	}
	
	
	public Page<UnionpayLog> paginateBySearch(int page, int pagesize, String keyword, String status, Long fromTime) {
		return paginate(page, pagesize, keyword, status, fromTime);
	}
	
	public Page<UnionpayLog> paginate(int page, int pagesize, String keyword, String status, Long fromTime) {

		String select = "select c.*";
		StringBuilder sql = new StringBuilder(" from unionpaylog c");
		LinkedList<Object> params = new LinkedList<Object>();
		sql.append(" left join user mc on c.user_id = mc.`id`");
		
		boolean needWhere = true;
		if (StringUtils.isNotBlank(keyword)) {
			needWhere = appendWhereOrAnd(sql, needWhere);
			sql.append(" ((mc.`realname` like ?) or (c.`pay_sn` like ?))");
			params.add(keyword);
			params.add("%" + keyword + "%");
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
	
	public UnionpayLog findByPaySn(String paySn) {
		if(StrKit.isBlank(paySn)) return null;
		LinkedList<Object> params = new LinkedList<Object>();
		StringBuilder sqlBuilder = new StringBuilder();
		appendAndIfNotEmpty(sqlBuilder, "pay_sn", paySn, params);
		return DAO.doFindFirst(sqlBuilder.toString(), params.toArray());
	}
	
	public double queryAmount(Integer status, Long fromTime, Long toTime){
		LinkedList<Object> params = new LinkedList<Object>();
		StringBuilder sqlBuilder = new StringBuilder("Select sum(fee) as money From unionpaylog Where 1 ");
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
		
		Double ret = Jdb.queryDouble(sqlBuilder.toString(), params.toArray());
		return ret == null ? 0.00 : ret;
	}
	
	public UnionpayLog findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


