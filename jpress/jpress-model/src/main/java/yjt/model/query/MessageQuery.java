package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import io.jpress.model.query.JBaseQuery;
import yjt.model.Message;

public class MessageQuery extends JBaseQuery{
	protected static final Message DAO = new Message();
	private static final MessageQuery QUERY = new MessageQuery();
	
	public static MessageQuery me(){
		return QUERY;
	}
	
	public List<Message> findList(int page, int pageSize, BigInteger fromUserId, BigInteger toUserId, Boolean isRead){
		StringBuilder sqlBuilder = new StringBuilder("Select * From message as c ");
		LinkedList<Object> params = new LinkedList<Object>();
		
		boolean needWhere = true;
		if(fromUserId != null){
			needWhere = appendIfNotEmpty(sqlBuilder, "c.from_userid", fromUserId.toString(), params, needWhere);
		}
		if(toUserId != null){
			needWhere = appendIfNotEmpty(sqlBuilder, "c.to_userid", toUserId.toString(), params, needWhere);
		}
		
		if(isRead != null){
			needWhere = appendIfNotEmpty(sqlBuilder, "c.is_read", isRead?"1":"0", params, needWhere);
		}
		
		sqlBuilder.append(" Order By c.id Desc Limit ?, ?");
		params.add((page -1)*pageSize);
		params.add(pageSize);
		if(params.isEmpty()){
			return DAO.find(sqlBuilder.toString());
		}else{
			return DAO.find(sqlBuilder.toString(), params.toArray());
		}
	}
	
	public long findCount( BigInteger fromUserId, BigInteger toUserId, Boolean isRead) {
		StringBuilder sqlBuilder = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();
		
		if(fromUserId != null){
			appendAndIfNotEmpty(sqlBuilder, "from_userid", fromUserId.toString(), params);
		}
		if(toUserId != null){
			appendAndIfNotEmpty(sqlBuilder, "to_userid", toUserId.toString(), params);
		}
		if(isRead != null){
			appendAndIfNotEmpty(sqlBuilder, "is_read", isRead?"1":"0", params);
		}
		
		if(params.isEmpty()){
			return DAO.doFindCount(sqlBuilder.toString());
		}else{
			return DAO.doFindCount(sqlBuilder.toString(), params.toArray());
		}
	}
}
