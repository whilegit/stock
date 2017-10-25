package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.query.JBaseQuery;
import io.jpress.utils.StringUtils;
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
	
	public Page<UnionpayLog> paginateBySearch(int page, int pagesize, String keyword, String status) {
		return paginate(page, pagesize, keyword, status);
	}
	
	public Page<UnionpayLog> paginate(int page, int pagesize, String keyword, String status) {

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
	
	public UnionpayLog findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


