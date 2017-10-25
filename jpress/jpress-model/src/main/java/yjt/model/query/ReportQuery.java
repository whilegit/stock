package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.query.JBaseQuery;
import io.jpress.utils.StringUtils;
import yjt.model.Report;

public class ReportQuery extends JBaseQuery{
	protected static final Report DAO = new Report();
	private static final ReportQuery QUERY = new ReportQuery();
	
	public static ReportQuery me(){
		return QUERY;
	}
	
	public long findCount(Report.Status stat) {
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
	
	public Page<Report> paginateBySearch(int page, int pagesize, String keyword, String status) {
		return paginate(page, pagesize, keyword, status);
	}
	
	public Page<Report> paginate(int page, int pagesize, String keyword, String status) {

		String select = "select c.*";
		StringBuilder sql = new StringBuilder(" from report c");
		LinkedList<Object> params = new LinkedList<Object>();
		sql.append(" left join user mf on c.from_userid = mf.`id`");
		sql.append(" left join user mt on c.to_userid = mt.`id`");
		
		boolean needWhere = true;
		if(StrKit.notBlank(status) && StringUtils.isNumeric(status)) {
			needWhere = appendIfNotEmpty(sql, "c.status", status, params, needWhere);
		}
		
		if (StringUtils.isNotBlank(keyword)) {
			needWhere = appendWhereOrAnd(sql, needWhere);
			sql.append(" ((mf.`realname` like ?) or (mt.`realname` like ?) or (c.`content` like ?))");
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
	
	public Report findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


