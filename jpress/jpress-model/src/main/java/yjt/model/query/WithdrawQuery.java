package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.query.JBaseQuery;
import io.jpress.utils.StringUtils;
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
	
	public Page<Withdraw> paginateBySearch(int page, int pagesize, String keyword, String status) {
		return paginate(page, pagesize, keyword, status);
	}
	
	public Page<Withdraw> paginate(int page, int pagesize, String keyword, String status) {

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
		
		sql.append(" group by c.id");
		sql.append(" ORDER BY c.id DESC");

		if (params.isEmpty()) {
			return DAO.paginate(page, pagesize, true, select, sql.toString());
		}

		return DAO.paginate(page, pagesize, true, select, sql.toString(), params.toArray());
	}
	
	public Withdraw findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


