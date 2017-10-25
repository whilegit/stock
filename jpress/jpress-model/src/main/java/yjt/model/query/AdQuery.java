package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.query.JBaseQuery;
import io.jpress.utils.StringUtils;
import yjt.model.Ad;

public class AdQuery extends JBaseQuery{
	protected static final Ad DAO = new Ad();
	private static final AdQuery QUERY = new AdQuery();
	
	public static AdQuery me(){
		return QUERY;
	}
	
	public long findCount(Ad.Status stat) {
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
	
	public List<Ad> findList(){
		StringBuilder sqlBuilder = new StringBuilder("Select * From ad ");
		LinkedList<Object> params = new LinkedList<Object>();
		sqlBuilder.append(" Order By order_num Desc");

		if(params.isEmpty()){
			return DAO.find(sqlBuilder.toString());
		}else{
			return DAO.find(sqlBuilder.toString(), params.toArray());
		}
	}
	
	public Page<Ad> paginateBySearch(int page, int pagesize, String keyword, String status) {
		return paginate(page, pagesize, keyword, status);
	}
	
	public Page<Ad> paginate(int page, int pagesize, String keyword, String status) {

		String select = "select c.*";
		StringBuilder sql = new StringBuilder(" from ad c");
		LinkedList<Object> params = new LinkedList<Object>();

		boolean needWhere = true;
		if(StrKit.notBlank(status) && StringUtils.isNumeric(status)) {
			needWhere = appendIfNotEmpty(sql, "c.status", status, params, needWhere);
		}
		
		sql.append(" group by c.id");
		sql.append(" ORDER BY c.order_num DESC");

		if (params.isEmpty()) {
			return DAO.paginate(page, pagesize, true, select, sql.toString());
		}

		return DAO.paginate(page, pagesize, true, select, sql.toString(), params.toArray());
	}
	
	public Ad findById(final BigInteger id) {
		return DAO.findById(id);
	}
	
	
}


