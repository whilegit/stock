package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.query.JBaseQuery;
import io.jpress.utils.StringUtils;
import yjt.model.Oplog;

public class OplogQuery extends JBaseQuery{
	protected static final Oplog DAO = new Oplog();
	private static final OplogQuery QUERY = new OplogQuery();
	
	public static OplogQuery me(){
		return QUERY;
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
	
	public Page<Oplog> paginateBySearch(int page, int pagesize, String keyword) {
		return paginate(page, pagesize, keyword);
	}
	
	public Page<Oplog> paginate(int page, int pagesize, String keyword) {

		String select = "select c.*";
		StringBuilder sql = new StringBuilder(" from oplog c");
		LinkedList<Object> params = new LinkedList<Object>();
		sql.append(" left join user mc on c.uid = mc.`id`");
		
		boolean needWhere = true;
		if (StringUtils.isNotBlank(keyword)) {
			needWhere = appendWhereOrAnd(sql, needWhere);
			sql.append(" ((mc.`realname` like ?) or (c.`name` like ?) or  (c.`op` like ?))");
			params.add(keyword);
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
	
	public Oplog findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


