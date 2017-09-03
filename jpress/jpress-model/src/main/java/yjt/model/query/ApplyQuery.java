package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;


import io.jpress.model.query.JBaseQuery;
import yjt.model.Apply;

public class ApplyQuery extends JBaseQuery{
	protected static final Apply DAO = new Apply();
	private static final ApplyQuery QUERY = new ApplyQuery();
	
	public static ApplyQuery me(){
		return QUERY;
	}
	
	public List<Apply> findList(Integer page, Integer pageSize, Apply.Status[] stats, BigInteger applyUid, BigInteger friendId){
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
	public Apply findById(final BigInteger id) {
		return DAO.findById(id);
	}
}


