package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;

import io.jpress.model.query.JBaseQuery;
import yjt.model.Follow;

public class FollowQuery extends JBaseQuery{
	protected static final Follow DAO = new Follow();
	private static final FollowQuery QUERY = new FollowQuery();
	
	public static FollowQuery me(){
		return QUERY;
	}
	
	public Follow getFollow(BigInteger followedId, BigInteger followerId){
		if(followedId == null || followerId == null) return null;
		
		StringBuilder sqlBuilder = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();
		

		appendAndIfNotEmpty(sqlBuilder, "followed_id", followedId.toString(), params);
		appendAndIfNotEmpty(sqlBuilder, "follower_id", followerId.toString(), params);

		Follow follow = DAO.doFindFirst(sqlBuilder.toString(), params.toArray());
		return follow;

	}
	
	
}
