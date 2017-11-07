package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.query.JBaseQuery;
import yjt.model.CreditLog;
import yjt.model.CreditLog.Platfrom;

public class CreditLogQuery extends JBaseQuery{
	protected static final CreditLog DAO = new CreditLog();
	private static final CreditLogQuery QUERY = new CreditLogQuery();
	
	public static CreditLogQuery me(){
		return QUERY;
	}
	
	public long findCount(BigInteger userID) {
		if(userID == null) return 0;
		
		StringBuilder sqlBuilder = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();
		appendAndIfNotEmpty(sqlBuilder, "user_id", userID.toString(), params);

		return DAO.doFindCount(sqlBuilder.toString(), params.toArray());
	}
	
	public List<CreditLog> findList(Integer page, Integer pageSize, BigInteger userID, Platfrom[] platforms){
		StringBuilder sqlBuilder = new StringBuilder("Select * From creditlog as c ");
		LinkedList<Object> params = new LinkedList<Object>();
		
		boolean needWhere = true;
		if(platforms != null && platforms.length > 0) {
			if(platforms.length == 1){
				sqlBuilder.append("Where c.platform = ? ");
				params.add(platforms[0].getIndex());
			} else {
				sqlBuilder.append("Where c.platform in (");
				for(int i = 0; i<platforms.length; i++){
					if(i != 0) sqlBuilder.append(",");
					sqlBuilder.append("?");
					params.add(platforms[i].getIndex());
				}
				sqlBuilder.append(") ");
			}
			needWhere = false;
		}
		
		String userIdStr = (userID != null) ? userID.toString() : null;
		needWhere = appendIfNotEmpty(sqlBuilder, "c.user_id", userIdStr, params, needWhere);
		
		if(page != null && pageSize != null){
			sqlBuilder.append(" Order By c.id Desc Limit ?, ?");
			params.add((page -1)*pageSize);
			params.add(pageSize);
		}
		if(params.isEmpty()){
			return DAO.find(sqlBuilder.toString());
		}else{
			return DAO.find(sqlBuilder.toString(), params.toArray());
		}
	}
	
	public Page<CreditLog> paginateBySearch(int page, int pagesize, BigInteger uid, Platfrom[] platforms) {
		return paginate(page, pagesize, uid, platforms);
	}
	
	public Page<CreditLog> paginate(int page, int pagesize,BigInteger uid, Platfrom[] platforms) {

		String select = "select c.*";
		StringBuilder sql = new StringBuilder(" from creditlog c Where 1 ");
		LinkedList<Object> params = new LinkedList<Object>();
		

		if(uid != null){
			appendIfNotEmpty(sql, "c.user_id", uid.toString(), params, false);
		}
		
		if(platforms != null && platforms.length > 0) {
			if(platforms.length == 1){
				sql.append("And c.platform = ? ");
				params.add(platforms[0].getIndex());
			} else {
				sql.append("And c.platform in (");
				for(int i = 0; i<platforms.length; i++){
					if(i != 0) sql.append(",");
					sql.append("?");
					params.add(platforms[i].getIndex());
				}
				sql.append(") ");
			}
		}
		
		sql.append(" group by c.id");
		sql.append(" ORDER BY c.id DESC");

		if (params.isEmpty()) {
			return DAO.paginate(page, pagesize, true, select, sql.toString());
		}

		return DAO.paginate(page, pagesize, true, select, sql.toString(), params.toArray());
	}
	
	public CreditLog findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


