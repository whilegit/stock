package yjt.model;

import io.jpress.model.User;
import io.jpress.model.core.Table;
import io.jpress.model.query.UserQuery;
import yjt.model.base.BaseFeedback;

@Table(tableName = "feedback", primaryKey = "id")
public class Feedback extends BaseFeedback<Feedback>{

	private static final long serialVersionUID = 1L;
	
	public User getUser() {
		User u = UserQuery.me().findByIdNoCache(this.getUserid());
		if(u == null) {
			u = new User();
		}
		return u;
	}
}
