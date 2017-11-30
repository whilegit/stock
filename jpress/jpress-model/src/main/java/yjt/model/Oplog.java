package yjt.model;

import java.math.BigInteger;
import java.util.Date;

import io.jpress.model.User;
import io.jpress.model.core.Table;
import io.jpress.model.query.UserQuery;
import yjt.model.base.BaseOplog;

@Table(tableName = "oplog", primaryKey = "id")
public class Oplog extends BaseOplog<Oplog>{

	private static final long serialVersionUID = 1L;
	
	public User getUser() {
		User u = UserQuery.me().findByIdNoCache(this.getUid());
		if(u == null) {
			u = new User();
		}
		return u;
	}
	
	public static boolean insertOp(BigInteger uid, String name, String type, String op, String ip) {
		Oplog oplog = new Oplog();
		oplog.setUid(uid);
		oplog.setName(name);
		oplog.setType(type);
		oplog.setCreateTime(new Date());
		oplog.setOp(op);
		oplog.setIp(ip);
		
		return oplog.save();
	}
}
