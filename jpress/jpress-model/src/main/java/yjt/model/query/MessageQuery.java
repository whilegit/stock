package yjt.model.query;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;

import io.jpress.model.query.JBaseQuery;
import yjt.model.Message;

public class MessageQuery extends JBaseQuery{
	protected static final SimpleDateFormat sdfYmdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		needWhere = appendIfNotEmpty(sqlBuilder, "c.deleted", "0", params, needWhere);
		
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
		appendAndIfNotEmpty(sqlBuilder, "deleted", "0", params);
		
		if(params.isEmpty()){
			return DAO.doFindCount(sqlBuilder.toString());
		}else{
			return DAO.doFindCount(sqlBuilder.toString(), params.toArray());
		}
	}
	
	public Message findById(final BigInteger id) {
		return DAO.findById(id);
	}
	
	/**
	 * 删除用户名下的所有消息(软删除)
	 * @param toUserID
	 * @return
	 */
	public int deleteAll(BigInteger toUserID) {
		if(toUserID == null) return 0;
		StringBuilder sqlBuilder = new StringBuilder("Update "+ DAO.getTableName() + " Set deleted=1");
		LinkedList<Object> params = new LinkedList<Object>();
		appendIfNotEmpty(sqlBuilder, "to_userid", toUserID.toString(), params, true);
		appendIfNotEmpty(sqlBuilder, "deleted", "0", params, false);
		return Db.update(sqlBuilder.toString(), params.toArray());
	}
	
	/**
	 * 设置用户名下的消息为已读状态
	 * @param toUserID
	 * @return
	 */
	public int readAll(BigInteger toUserID) {
		if(toUserID == null) return 0;
		StringBuilder sqlBuilder = new StringBuilder("Update "+ DAO.getTableName() + " Set is_read=1,read_time='"+sdfYmdHms.format(new Date())+"'");
		LinkedList<Object> params = new LinkedList<Object>();
		appendIfNotEmpty(sqlBuilder, "to_userid", toUserID.toString(), params, true);
		appendIfNotEmpty(sqlBuilder, "is_read", "0", params, false);
		appendIfNotEmpty(sqlBuilder, "deleted", "0", params, false);
		return Db.update(sqlBuilder.toString(), params.toArray());
	}
}
