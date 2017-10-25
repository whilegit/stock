package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.query.JBaseQuery;
import io.jpress.utils.StringUtils;
import yjt.model.Feedback;

public class FeedbackQuery extends JBaseQuery{
	protected static final Feedback DAO = new Feedback();
	private static final FeedbackQuery QUERY = new FeedbackQuery();
	
	public static FeedbackQuery me(){
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
	
	public Page<Feedback> paginateBySearch(int page, int pagesize, String keyword, String status) {
		return paginate(page, pagesize, keyword, status);
	}
	
	public Page<Feedback> paginate(int page, int pagesize, String keyword, String status) {

		String select = "select c.*";
		StringBuilder sql = new StringBuilder(" from feedback c");
		LinkedList<Object> params = new LinkedList<Object>();
		sql.append(" left join user mc on c.userid = mc.`id`");
		
		boolean needWhere = true;
		if (StringUtils.isNotBlank(keyword)) {
			needWhere = appendWhereOrAnd(sql, needWhere);
			sql.append(" ((mc.`realname` like ?) or (c.`name` like ?) or  (c.`content` like ?))");
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
	
	public Feedback findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


